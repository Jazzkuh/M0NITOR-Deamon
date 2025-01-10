package com.jazzkuh.m0nitor.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@UtilityClass
public class RequestUtils {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @SneakyThrows
    public CompletableFuture<JsonObject> get(String url, String... authorization) {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                if (authorization.length > 0) {
                    connection.setRequestProperty("Authorization", authorization[0]);
                }

                InputStream inputStream = connection.getInputStream();
                JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
                future.complete(jsonObject);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
