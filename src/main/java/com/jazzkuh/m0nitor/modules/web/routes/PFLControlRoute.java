package com.jazzkuh.m0nitor.modules.web.routes;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.Fader;
import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import com.jazzkuh.m0nitor.modules.web.WebModule;

import static spark.Spark.get;

public final class PFLControlRoute {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);

    public PFLControlRoute(WebModule webModule) {
        get(Route.PFL_CONTROL.getPath(), (request, response) -> {
            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            String value = request.params(":value");
            switch (value.toLowerCase()) {
                case "reset":
                    udpModule.writeToSocket((byte) 0x02, (byte) 0x07);
                    break;
                case "autoann":
                    udpModule.writeToSocket((byte) 0x03, (byte) 0x08, (byte) 0x02);
                    break;
                case "autocrm":
                    udpModule.writeToSocket((byte) 0x03, (byte) 0x09, (byte) 0x02);
                    break;
                case "aux":
                    udpModule.writeToSocket((byte) 0x04, (byte) 0x06, (byte) 0x08, (byte) 0x02);
                    break;
                default:
                    response.body(getError("Invalid value").toString());
                    return response.body();
            }

            response.body(getSuccess("PFL Control updated").toString());
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
