package com.jazzkuh.m0nitor.modules.web.routes;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.Fader;
import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import com.jazzkuh.m0nitor.modules.web.WebModule;

import static spark.Spark.get;

public final class ChannelRoute {
    private final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);

    public ChannelRoute(WebModule webModule) {
        get(Route.CHANNEL_STATE.getPath(), (request, response) -> {
            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            int channel = Integer.parseInt(request.params(":channel"));
            Fader fader = airliteModule.getFaders().get(channel);
            if (fader == null) {
                response.body(getError("Channel not found").toString());
                return response.body();
            }

            if (request.params(":state").equalsIgnoreCase("toggle")) {
                udpModule.writeRemoteOn(fader, !fader.isChannelOn());
                return "OK";
            }

            boolean state = Boolean.parseBoolean(request.params(":state"));
            udpModule.writeRemoteOn(fader, state);

            response.body(getSuccess("Channel state updated").toString());
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
