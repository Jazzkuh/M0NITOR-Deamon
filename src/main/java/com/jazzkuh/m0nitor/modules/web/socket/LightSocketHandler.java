package com.jazzkuh.m0nitor.modules.web.socket;


import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.utils.Concurrency;
import io.github.zeroone3010.yahueapi.v2.HueEventListener;
import io.github.zeroone3010.yahueapi.v2.domain.HueEvent;
import lombok.SneakyThrows;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.*;

@WebSocket
public class LightSocketHandler implements HueEventListener {
    private final WebModule webModule = Deamon.getModuleManager().get(WebModule.class);
    private final List<Session> sessions = new ArrayList<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
        sendMessage();
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @Override
    public void receive(List<HueEvent> events) {
        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                sendMessage();
            }
        }, 1000);
    }

    @SneakyThrows
    public void sendMessage() {
        Concurrency.async().execute(() -> {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        JsonObject jsonObject = webModule.getLights();
                        session.getRemote().sendString(jsonObject.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}