package com.jazzkuh.m0nitor.modules.web.socket;


import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class LightSocketHandler {
    private final WebModule webModule = Deamon.getModuleManager().get(WebModule.class);

    @OnWebSocketConnect
    public void onConnect(Session session) {
        webModule.getLightSessions().add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        webModule.getLightSessions().remove(session);
    }
}