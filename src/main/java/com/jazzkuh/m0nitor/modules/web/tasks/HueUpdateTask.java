package com.jazzkuh.m0nitor.modules.web.tasks;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.modules.web.socket.LightSocketHandler;
import com.jazzkuh.m0nitor.utils.Concurrency;
import com.jazzkuh.modulemanager.generic.handlers.tasks.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.TimerTask;

@AllArgsConstructor
@TaskInfo(period = 500, delay = 500, repeating = true)
public class HueUpdateTask extends TimerTask {
    private final WebModule webModule;

    @Override
    @SneakyThrows
    public void run() {
        Concurrency.async().execute(() -> {
            for (Session session : webModule.getLightSessions()) {
                if (session.isOpen()) {
                    try {
                        JsonObject jsonObject = webModule.getLights();
                        session.getRemote().sendString(jsonObject.toString());
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }
}
