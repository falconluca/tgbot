package me.shaohsiung.cmd;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.dao.SQLiteWordSpecDao;
import me.shaohsiung.dao.WordSpecDao;
import me.shaohsiung.enums.PronunciationEnums;
import me.shaohsiung.enums.SounderEnums;
import me.shaohsiung.job.WordJob;
import me.shaohsiung.model.Pronunciation;
import me.shaohsiung.model.WordSpec;
import me.shaohsiung.response.BaseResponse;
import me.shaohsiung.sounder.GoogleSounder;
import me.shaohsiung.sounder.YouTubeSounder;
import me.shaohsiung.sounder.YoudaoSounder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.util.EntityUtils;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class WordCommand {
    private static final int MAX_WAIT_SECONDS = 5;
    
    private final SilentSender silent;
    
    private final Map<String, WordJob> jobs;
    
    private final GoogleSounder googleSounder;
    
    private final YoudaoSounder youdaoSounder;
    
    private final YouTubeSounder youTubeSounder;
    
    private final CloseableHttpAsyncClient httpclient;
    
    private final WordSpecDao wordSpecDao;
    
    public WordCommand(SilentSender silent, Map<String, WordJob> jobs) {
        this.silent = silent;
        this.jobs = jobs;
        
        this.googleSounder = new GoogleSounder();
        this.youdaoSounder = new YoudaoSounder();
        this.youTubeSounder = new YouTubeSounder();
        
        this.wordSpecDao = new SQLiteWordSpecDao();

        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(3000)
                .setSoTimeout(3000)
                .setIoThreadCount(Runtime.getRuntime().availableProcessors() * 2)
                .build();
        httpclient = HttpAsyncClients.custom()
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(final String method) {
                        return false;
                    }
                }).setMaxConnTotal(4000)
                .setMaxConnPerRoute(1000)
                .setDefaultIOReactorConfig(ioConfig)
                .build();
        httpclient.start();
    }

    public void action(MessageContext ctx) {
        String word = ctx.firstArg();
        if (StringUtils.isBlank(word)) {
            silent.send("🌚 Oops! Something wrong...\n " +
                    "please enter at least one word\n\n🎯 Example: /word hello\n", ctx.chatId());
            return;
        }

        List<Pronunciation> pronunciationList = composePronunciationList(word);
        List<BaseResponse> explanationList = fetchExplanationAndSyncWord(word);
        WordSpec wordSpec = buildWordSpec(word, pronunciationList, explanationList);
        wordSpecDao.persist(wordSpec);
        String result = wordSpec.beautyFormat();
        silent.send(result, ctx.chatId());
    }

    private WordSpec buildWordSpec(String word, List<Pronunciation> pronunciationList, 
            List<BaseResponse> explanationList) {
        WordSpec wordSpec = new WordSpec();
        wordSpec.setWord(word);
        wordSpec.setCreateAt(LocalDateTime.now());
        wordSpec.setVideoList(Collections.singletonList(youTubeSounder.findFirst(word)));
        
        for (Pronunciation pronunciation : pronunciationList) {
            pronunciation.attach(wordSpec);
        }
        for (BaseResponse explanation : explanationList) {
            explanation.attach(wordSpec);
        }
        // TODO add some images
        wordSpec.setImgList(Collections.emptyList());
        return wordSpec;
    }

    private List<BaseResponse> fetchExplanationAndSyncWord(String word) {
        final List<BaseResponse> result = new CopyOnWriteArrayList<>();
        
        final AtomicLong success = new AtomicLong();
        final AtomicLong fail = new AtomicLong();
        
        final CountDownLatch latch = new CountDownLatch(jobs.size());
        for (Map.Entry<String, WordJob> entry : jobs.entrySet()) {
            WordJob job = entry.getValue();
            HttpUriRequest request = job.prepareRequest(word);
            httpclient.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(final HttpResponse response) {
                    try {
                        handleResponse(response, job, result);
                        success.incrementAndGet();
                    }
                    catch (Exception e) {
                        log.error(job.getUrl() + " error:", e);
                    }
                    finally {
                        latch.countDown();
                    }
                }

                @Override
                public void failed(final Exception ex) {
                    latch.countDown();
                    fail.incrementAndGet();
                    request.abort();
                    if (ex instanceof SocketTimeoutException) {
                        log.error("Failed to fetch metric from <{}>: socket timeout", job.getUrl());
                    }
                    else if (ex instanceof ConnectException) {
                        log.error("Failed to fetch metric from <{}> (ConnectionException: {})", 
                                job.getUrl(), ex.getMessage());
                    }
                    else {
                        log.error(job.getUrl() + " error", ex);
                    }
                }

                @Override
                public void cancelled() {
                    latch.countDown();
                    fail.incrementAndGet();
                    request.abort();
                }
            });
        }

        try {
            latch.await(MAX_WAIT_SECONDS, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            log.info("wait http client error:", e);
        }
        log.info("fetch explanation and sync word ==> success: {}, fail: {}", success, fail);
        return result;
    }
    
    private void handleResponse(HttpResponse response, WordJob job, List<BaseResponse> result) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        log.info("HTTP Response status line: " + statusLine.toString());

        Charset charset = null;
        try {
            String contentTypeStr = response.getFirstHeader("Content-type").getValue();
            if (StringUtils.isNoneBlank(contentTypeStr)) {
                ContentType contentType = ContentType.parse(contentTypeStr);
                charset = contentType.getCharset();
            }
        }
        catch (Exception ignore) {}
        String body = EntityUtils.toString(response.getEntity(), charset != null ? charset : Charset.defaultCharset());
        if (StringUtils.isEmpty(body)) {
            log.info(job.getKey() + ", bodyStr is empty");
            return;
        }

        List<BaseResponse> data = job.handleResponse(body);
        result.addAll(data);
    }

    private List<Pronunciation> composePronunciationList(String word) {
        if (StringUtils.isBlank(word)) {
            return Collections.emptyList();
        }
        
        final List<Pronunciation> result = new ArrayList<>();
        String w = word.trim();
        
        String british = youdaoSounder.british(w);
        result.add(Pronunciation.of(w, british, SounderEnums.YOUDAO.name(), PronunciationEnums.BRITISH));

        String usa = youdaoSounder.usa(w);
        result.add(Pronunciation.of(w, usa, SounderEnums.YOUDAO.name(), PronunciationEnums.AMERICAN));

        String google = googleSounder.pronounce(w);
        result.add(Pronunciation.of(w, google, SounderEnums.GOOGLE.name(), null));
        return result;
    }
}
