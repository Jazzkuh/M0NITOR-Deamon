package com.jazzkuh.m0nitor.utils.music;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.utils.FileUtils;
import com.jazzkuh.modulemanager.generic.handlers.tasks.TaskInfo;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.TimerTask;

public class SpotifyTokenManager extends TimerTask {
    @Getter
    private static String cachedToken = null;

    private static long tokenExpiration = 0;
    private static final HttpClient client = HttpClient.newHttpClient();

    @Override
    public void run() {
        if (cachedToken != null && tokenExpiration > System.currentTimeMillis()) {
            return;
        }

        generateToken();
    }

    @SneakyThrows
    private String generateToken() {
        String cookie = FileUtils.readFileFromResources("cookie.txt");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://open.spotify.com/get_access_token?reason=transport&productType=web_player"))
                .header("Cookie", cookie)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String accessToken = jsonObject.get("accessToken").getAsString();
        long accessTokenExpirationTimestampMs = Long.parseLong(jsonObject.get("accessTokenExpirationTimestampMs").getAsString());

        cachedToken = accessToken;
        tokenExpiration = accessTokenExpirationTimestampMs;

        System.out.println("Generated new Spotify access token: " + accessToken);
        return accessToken;
    }
}