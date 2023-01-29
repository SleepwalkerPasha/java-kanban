package com.taskmanager.client;

import com.google.gson.Gson;
import com.taskmanager.exception.ManagerSaveException;
import com.taskmanager.server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final URI url;

    private final String apiToken;

    private final HttpClient client;


    public KVTaskClient(URI url, String apiToken) throws IOException {
        this.url = url;
        this.apiToken = apiToken;
        client = HttpClient.newHttpClient();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI url1 = URI.create(this.url.toString() + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(url1)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
            throw new ManagerSaveException("Произошла ошибка при сохранении");
        System.out.println(response.body());
        System.out.println(response.statusCode());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI url1 = URI.create(this.url.toString() + "/load/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url1)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
            throw new ManagerSaveException("Произошла ошибка при сохранении");
        System.out.println(response.body());
        System.out.println(response.statusCode());
        return response.body();
    }
}
