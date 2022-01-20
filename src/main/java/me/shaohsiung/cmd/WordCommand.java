package me.shaohsiung.cmd;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.enums.SoundEnums;
import me.shaohsiung.job.WordJob;
import me.shaohsiung.model.BaseModel;
import me.shaohsiung.model.EudbResponse;
import me.shaohsiung.model.SoundData;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
public class WordCommand {
    private final SilentSender silent;
    
    private Map<String, WordJob> jobs;
    
    private final GoogleSounder googleSounder;
    
    private final YoudaoSounder youdaoSounder;
    
    private final YouTubeSounder youTubeSounder;
    
    private final CloseableHttpAsyncClient httpclient;
    
    private static final int MAX_WAIT_SECONDS = 5;
    
    public WordCommand(SilentSender silent, Map<String, WordJob> jobs) {
        this.silent = silent;
        this.jobs = jobs;
        
        this.googleSounder = new GoogleSounder();
        this.youdaoSounder = new YoudaoSounder();
        this.youTubeSounder = new YouTubeSounder();

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
        String word = "hello";
        
        List<BaseModel> soundDataList = buildSoundDataList(word);
        List<BaseModel> wordDataList = fetchAndSyncWordData(word);
        List<BaseModel> wordDataWithoutEudb = wordDataList.stream()
                .filter(item -> !(item instanceof EudbResponse))
                .collect(Collectors.toList());
        List<BaseModel> data = addAll(soundDataList, wordDataWithoutEudb);

        // sqlite and ui string -> concurrency
        
        silent.send("pong", ctx.chatId());
    }

    private List<BaseModel> addAll(List<BaseModel> soundDataList, List<BaseModel> wordDataList) {
        List<BaseModel> result = new ArrayList<>(soundDataList.size() + wordDataList.size());
        result.addAll(soundDataList);
        result.addAll(wordDataList);
        return result;
    }

    private List<BaseModel> fetchAndSyncWordData(String word) {
        final List<BaseModel> result = new CopyOnWriteArrayList<>();
        
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
        return result;
    }
    
    private void handleResponse(HttpResponse response, WordJob job, List<BaseModel> result) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        System.out.println(statusLine.toString());

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

        job.handleResponse(body);
    }

    private List<BaseModel> buildSoundDataList(String word) {
        if (StringUtils.isBlank(word)) {
            return Collections.emptyList();
        }
        
        final List<BaseModel> result = new ArrayList<>();
        String w = word.trim();
        
        String british = youdaoSounder.british(w);
        result.add(SoundData.of(british, SoundEnums.YOUDAO.name()));

        String usa = youdaoSounder.usa(w);
        result.add(SoundData.of(usa, SoundEnums.YOUDAO.name()));

        String youtube = youTubeSounder.list(w);
        result.add(SoundData.of(youtube, SoundEnums.YOUTUBE.name()));

        String google = googleSounder.pronounce(w);
        result.add(SoundData.of(google, SoundEnums.GOOGLE.name()));
        return result;
    }
}
