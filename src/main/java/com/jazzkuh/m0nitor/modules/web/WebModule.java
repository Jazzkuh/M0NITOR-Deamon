package com.jazzkuh.m0nitor.modules.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.framework.airlite.Fader;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import com.jazzkuh.m0nitor.modules.web.server.WebServer;
import com.jazzkuh.m0nitor.utils.Concurrency;
import com.jazzkuh.m0nitor.utils.FileUtils;
import com.jazzkuh.modulemanager.generic.GenericModule;
import com.jazzkuh.modulemanager.generic.GenericModuleManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.eclipse.jetty.websocket.api.Session;
import spark.Request;
import spark.Response;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebModule extends GenericModule {
    @Getter
    private ExecutorService webExecutors = Executors.newFixedThreadPool(1);

    @Getter
    private Set<Session> sessions = new HashSet<>();

    @Getter
    private WebServer webServer;

    private AirliteModule airliteModule;
    private UDPModule udpModule;

    public WebModule(GenericModuleManager owningManager, AirliteModule airliteModule) {
        super(owningManager);
    }

    @Override
    public void onEnable() {
        this.airliteModule = getOwningManager().get(AirliteModule.class);
        this.udpModule = getOwningManager().get(UDPModule.class);
        this.webServer = new WebServer(this, 8082);
    }

    @Override
    public void onDisable() {
        System.out.println("WebModule disabled");
    }

    public JsonObject getJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);

        JsonArray faders = new JsonArray();
        for (int i = 1; i <= 8; i++) {
            JsonObject faderObject = new JsonObject();
            Fader fader = airliteModule.getFaders().get(i);
            faderObject.addProperty("channel_id", fader.getChannelId());
            faderObject.addProperty("fader_active", fader.isFaderActive());
            faderObject.addProperty("channel_on", fader.isChannelOn());
            faderObject.addProperty("cue_active", fader.isCueActive());
            faders.add(faderObject);
        }
        jsonObject.add("faders", faders);

        JsonObject meteringValues = new JsonObject();
        for (String key : udpModule.getMeteringValues().keySet()) {
            double value = udpModule.getMeteringValues().get(key);
            meteringValues.addProperty(key, value > 55 ? 55 : value);
        }
        jsonObject.add("metering", meteringValues);

        jsonObject.addProperty("microphone_on", airliteModule.getMicrophoneOn() != -1);

        SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("HH:mm:ss");
        jsonObject.addProperty("time", dateFormat.format(new Date()));

        dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));
        jsonObject.addProperty("microphone_on_since", airliteModule.getMicrophoneOn());

        Date elapsed = new Date(System.currentTimeMillis() - airliteModule.getMicrophoneOn());
        jsonObject.addProperty("microphone_on_time", dateFormat.format(elapsed));

        jsonObject.addProperty("on_air", airliteModule.getFaders().values().stream().anyMatch(faderStatus -> faderStatus.isChannelOn() && faderStatus.isFaderActive()));
        jsonObject.addProperty("cue_enabled", airliteModule.getFaders().values().stream().anyMatch(Fader::isCueActive) || airliteModule.isCueAux());
        jsonObject.addProperty("auto_cue_crm", airliteModule.isAutoCueCrm());
        jsonObject.addProperty("auto_cue_announcer", airliteModule.isAutoCueAnnouncer());
        jsonObject.addProperty("cue_aux", airliteModule.isCueAux());

        jsonObject.add("spotify", airliteModule.getSpotifyJson());
        return jsonObject;
    }

    @SneakyThrows
    public void sendToSocket(JsonObject jsonObject) {
        Concurrency.async().execute(() -> getSessions().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(jsonObject.toString());
            } catch (Exception exception) {
                getLogger().warn("Failed to send message to websocket: {}\n{}", exception.getMessage(), exception.getStackTrace());
            }
        }));
    }

    @SneakyThrows
    public boolean authorized(Request request, Response response) {
        String apiKey = request.headers("X-API-Key");

        String requiredKey = FileUtils.readFileFromResources("cookie.txt");
        if (apiKey == null || !apiKey.equals(requiredKey)) {
            JsonObject object = new JsonObject();
            object.addProperty("success", false);
            object.addProperty("message", "You are not authorized to access this route.");
            response.body(object.toString());
            return false;
        }

        return true;
    }
}
