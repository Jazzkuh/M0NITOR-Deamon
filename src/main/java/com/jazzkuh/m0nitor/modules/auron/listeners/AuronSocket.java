package com.jazzkuh.m0nitor.modules.auron.listeners;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.modules.auron.processing.AuronProcessor;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class AuronSocket extends WebSocketClient {
    private final AuronModule auronModule;
    private final WebModule webModule;

    public AuronSocket(AuronModule auronModule, WebModule webModule) {
        super(URI.create("ws://192.168.1.47/AURON"));
        this.auronModule = auronModule;
        this.webModule = webModule;
        setConnectionLostTimeout(30);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to the AURON WebSocket server.");

        JsonArray paramArray = new JsonArray();
        paramArray.add("ctrl_key");
        paramArray.add("module_active");
        paramArray.add("on_air");
        paramArray.add("cue");
        paramArray.add("fader");
        paramArray.add("on");
        paramArray.add("config_route");
        paramArray.add("metering:master");

        auronModule.sendToSocket("subscribe", paramArray);
        auronModule.sendToSocket("get_all", new JsonArray());
    }

    @Override
    @SneakyThrows
    public void onMessage(String data) {
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        String msg = jsonObject.get("msg").getAsString();

        AuronProcessor auronProcessor = new AuronProcessor();
        try {
            webModule.sendToSocket(webModule.getJson());

            JsonObject param = jsonObject.getAsJsonObject("param");
            auronProcessor.process(msg, param);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected, reconnecting...");
        reconnect();
    }

    @Override
    public void onError(Exception e) {
    }
}