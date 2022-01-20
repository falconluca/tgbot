package me.shaohsiung.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// https://openjdk.java.net/groups/net/httpclient/recipes.html
public class RestClient {
    private final HttpClient client;

    public RestClient() {
        this.client = HttpClient.newHttpClient();
    }
    
    public <T> String post(String url, T data) {
        String json = JsonUtils.toJson(data);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> resp;
        try {
            resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        } 
        catch (IOException | InterruptedException e) {
            String msg = String.format("http post request failed. url: %s, data: %s", url, data);
            throw new RuntimeException(msg, e);
        }
        return resp.body();
    }
}
