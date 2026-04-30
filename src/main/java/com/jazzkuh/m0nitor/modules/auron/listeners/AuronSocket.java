package com.jazzkuh.m0nitor.modules.auron.listeners;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.modules.auron.processing.AuronProcessor;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;

import java.net.URI;
import java.util.List;

public class AuronSocket extends WebSocketClient {
    private static final Logger LOGGER = Deamon.getLogger();
    private static final List<String> SUBSCRIPTIONS = List.of(
            "ctrl_key",
            "module_active",
            "on_air",
            "cue",
            "fader",
            "on",
            "config_route",
            "value_changed",
            "metering:master"
    );

    private final AuronModule auronModule;
    private final WebModule webModule;
    private final AuronProcessor auronProcessor = new AuronProcessor();

    public AuronSocket(AuronModule auronModule, WebModule webModule) {
        super(URI.create("ws://192.168.1.47/AURON"));
        this.auronModule = auronModule;
        this.webModule = webModule;
        setConnectionLostTimeout(30);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.info("Connected to the AURON WebSocket server.");

        JsonArray paramArray = new JsonArray();
        SUBSCRIPTIONS.forEach(paramArray::add);

        auronModule.sendToSocket("subscribe", paramArray);
        auronModule.sendToSocket("get_all", new JsonArray());
    }

    @Override
    public void onMessage(String data) {
        try {
            JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
            String msg = jsonObject.get("msg").getAsString();
            JsonElement paramElement = jsonObject.get("param");

            if (paramElement != null && paramElement.isJsonObject()) {
                auronProcessor.process(msg, paramElement.getAsJsonObject());
            }

            if (webModule.isEnabled()) {
                webModule.sendToSocket(webModule.getJson());
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to process message: {}", data, e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.warn("Disconnected (code={}, reason={}), reconnecting...", code, reason);
        reconnect();
    }

    @Override
    public void onError(Exception e) {
        LOGGER.error("WebSocket error", e);
    }
}