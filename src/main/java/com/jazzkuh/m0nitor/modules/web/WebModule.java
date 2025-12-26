package com.jazzkuh.m0nitor.modules.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
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

public class WebModule extends GenericModule {
    @Getter
    private final Set<Session> sessions = new HashSet<>();

    @Getter
    private WebServer webServer;

    private AuronModule auronModule;

    public WebModule(GenericModuleManager owningManager, AuronModule auronModule) {
        super(owningManager);
    }

    @Override
    public void onEnable() {
        this.auronModule = getOwningManager().get(AuronModule.class);
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
        for (int i = 1; i <= 10; i++) {
            JsonObject faderObject = new JsonObject();
            Module module = auronModule.getModules().get(i);
            faderObject.addProperty("name", module.getName());
            faderObject.addProperty("channel_id", module.getChannelId());
            faderObject.addProperty("fader_active", module.isFaderActive());
            faderObject.addProperty("channel_on", module.isActive());
            faderObject.addProperty("cue_active", module.isCue());
            faderObject.addProperty("fader_level", module.getFaderLevel());
            faderObject.addProperty("left", module.getLeft());
            faderObject.addProperty("right", module.getRight());
            faders.add(faderObject);
        }
        jsonObject.add("faders", faders);

        JsonObject meteringValues = new JsonObject();
        for (String key : auronModule.getMeteringValues().keySet()) {
            double value = auronModule.getMeteringValues().get(key);
            meteringValues.addProperty(key, value);
        }

        if (auronModule.getMeteringValues().isEmpty()) {
            meteringValues.addProperty("program_left", 0);
            meteringValues.addProperty("program_right", 0);
            meteringValues.addProperty("phones_left", 0);
            meteringValues.addProperty("phones_right", 0);
            meteringValues.addProperty("crm_left", 0);
            meteringValues.addProperty("crm_right", 0);
            meteringValues.addProperty("aux_left", 0);
            meteringValues.addProperty("aux_right", 0);
            meteringValues.addProperty("master_left", 0);
            meteringValues.addProperty("master_right", 0);
            meteringValues.addProperty("sub_left", 0);
            meteringValues.addProperty("sub_right", 0);
            meteringValues.addProperty("studio_left", 0);
            meteringValues.addProperty("studio_right", 0);
            meteringValues.addProperty("vt_left", 0);
            meteringValues.addProperty("vt_right", 0);
        }
        jsonObject.add("metering", meteringValues);

        jsonObject.addProperty("microphone_on", auronModule.getMicrophoneOn() != -1);

        SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("HH:mm:ss");
        jsonObject.addProperty("time", dateFormat.format(new Date()));

        dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));
        jsonObject.addProperty("microphone_on_since", auronModule.getMicrophoneOn());

        Date elapsed = new Date(System.currentTimeMillis() - auronModule.getMicrophoneOn());
        jsonObject.addProperty("microphone_on_time", dateFormat.format(elapsed));

        jsonObject.addProperty("on_air", auronModule.getModules().values().stream().anyMatch(Module::isActive));
        jsonObject.addProperty("auto_cue_crm", auronModule.isAutoCueCrm());
        jsonObject.addProperty("auto_cue_announcer", auronModule.isAutoCueAnnouncer());
        jsonObject.addProperty("cue_enabled", auronModule.isCueAux());
        jsonObject.addProperty("cue_aux", auronModule.isCueAux());
        jsonObject.addProperty("cue_air", auronModule.isCueAir());

        jsonObject.add("spotify", auronModule.getSpotifyJson());
        return jsonObject;
    }

    @SneakyThrows
    public void sendToSocket(JsonObject jsonObject) {
        Concurrency.async().execute(() -> getSessions().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(jsonObject.toString());
            } catch (Exception ignored) {
            }
        }));
    }

    @SneakyThrows
    public boolean authorized(Request request, Response response) {
        String apiKey = request.headers("X-API-Key");

        String requiredKey = FileUtils.readFileFromResources("api-key.txt");
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
