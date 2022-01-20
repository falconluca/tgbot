package me.shaohsiung.cmd;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.jetbrains.annotations.NotNull;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class WordCommand {
    private final SilentSender silent;
    
    private CloseableHttpAsyncClient httpclient;
    
    private static final int MAX_WAIT_SECONDS = 5;
    private final String eudbAccessToken;
    
    public WordCommand(SilentSender silent, String eudbAccessToken) {
        this.silent = silent;
        this.eudbAccessToken = eudbAccessToken;

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
        Map<String, String> jobs = new ConcurrentHashMap<>();
        jobs.put("柯林斯词典", "https://www.collinsdictionary.com/dictionary/english/hello");
        jobs.put("牛津词典", "https://www.oxfordlearnersdictionaries.com/definition/american_english/hello");
        jobs.put("欧陆词典", "https://api.frdic.com/api/open/v1/studylist/words,hello,132871361769636420");
        
        final AtomicLong success = new AtomicLong();
        final AtomicLong fail = new AtomicLong();
        final CountDownLatch latch = new CountDownLatch(jobs.size());
        for (Map.Entry<String, String> entry : jobs.entrySet()) {
            String url = entry.getValue();
            final HttpUriRequest request = prepareRequest(entry.getKey(), url);
            httpclient.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(final HttpResponse response) {
                    try {
                        handleResponse(response, entry.getKey());
                        success.incrementAndGet();
                    } 
                    catch (Exception e) {
                        log.error(url + " error:", e);
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
                        log.error("Failed to fetch metric from <{}>: socket timeout", url);
                    } 
                    else if (ex instanceof ConnectException) {
                        log.error("Failed to fetch metric from <{}> (ConnectionException: {})", url, ex.getMessage());
                    } 
                    else {
                        log.error(url + " error", ex);
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
        silent.send("pong", ctx.chatId());
        System.out.println("done");
    }

    @NotNull
    private HttpUriRequest prepareRequest(String key, String url) {
        if ("柯林斯词典".equals(key) || "牛津词典".equals(key)) {
            final HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
            httpGet.setHeader(HTTP.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36v");
            return httpGet;
        }
        
        if ("欧陆词典".equals(key)) {
            String[] urls = url.split(",");
            final HttpPost httPost = new HttpPost(urls[0]);
            String word = urls[1];
            String bookId = urls[2];
            String JSON_STRING="{\n" +
                    "    \"id\": \""+  bookId +"\",\n" +
                    "    \"language\": \"en\",\n" +
                    "    \"words\": [\n" +
                    "        \"" + word + "\"\n" +
                    "    ]\n" +
                    "}";
            HttpEntity stringEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            httPost.setEntity(stringEntity);
            httPost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
            httPost.setHeader(HTTP.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36v");
            httPost.setHeader("Authorization", eudbAccessToken);
            return httPost;
        }
        else {
            throw new RuntimeException("error");
        }
    }

    private void handleResponse(HttpResponse response, String key) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        System.out.println(statusLine.toString());

//        Charset charset = null;
//        try {
//            String contentTypeStr = response.getFirstHeader("Content-type").getValue();
//            if (StringUtils.isNoneBlank(contentTypeStr)) {
//                ContentType contentType = ContentType.parse(contentTypeStr);
//                charset = contentType.getCharset();
//            }
//        } catch (Exception ignore) {
//        }
//
//        String body = EntityUtils.toString(response.getEntity(), charset != null ? charset : Charset.defaultCharset());
//        if (StringUtils.isEmpty(body)) {
//            //logger.info(machine.getApp() + ":" + machine.getIp() + ":" + machine.getPort() + ", bodyStr is empty");
//            return;
//        }
//        String[] lines = body.split("\n");
//        System.out.println(lines);
    }
}
