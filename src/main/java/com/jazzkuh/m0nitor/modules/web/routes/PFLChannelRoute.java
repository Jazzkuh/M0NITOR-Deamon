package com.jazzkuh.m0nitor.modules.web.routes;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.modules.web.WebModule;

import static spark.Spark.get;

public final class PFLChannelRoute {
    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    public PFLChannelRoute(WebModule webModule) {
        get(Route.PFL_CHANNEL.getPath(), (request, response) -> {
            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            int channel = Integer.parseInt(request.params(":channel"));
            Module module = auronModule.getModules().get(channel);
            if (module == null) {
                response.body(getError("Channel not found").toString());
                return response.body();
            }

            JsonObject param = new JsonObject();
            param.addProperty("module_" + channel, !module.isCue());
            auronModule.sendToSocket("set_cue", param);

            response.body(getSuccess("PFL Channel updated").toString());
            return response.body();
        });
    }

    public JsonObject getError(String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        jsonObject.addProperty("message", message);
        return jsonObject;
    }

    public JsonObject getSuccess(String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);
        jsonObject.addProperty("message", message);
        return jsonObject;
    }
}
