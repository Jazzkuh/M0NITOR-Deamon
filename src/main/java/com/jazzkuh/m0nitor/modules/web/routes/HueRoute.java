package com.jazzkuh.m0nitor.modules.web.routes;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import io.github.zeroone3010.yahueapi.v2.Light;

import static spark.Spark.get;

public final class HueRoute {
    private final HueController hueController = Deamon.getInstance().getHueController();

    public HueRoute(WebModule webModule) {
        get(Route.HUE.getPath(), (request, response) -> {
            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            String action = request.params(":action");
            String light = request.params(":light");

            Light hueLight = hueController.getLightByName(light);
            if (hueLight == null) {
                response.body(getError("Light not found").toString());
                return response.body();
            }

            switch (action.toLowerCase()) {
                case "toggle":
                    hueController.setLightState(hueLight, !hueLight.isOn());
                    break;
                case "on":
                    if (hueLight.isOn()) break;
                    hueController.setLightState(hueLight, true);
                    break;
                case "off":
                    if (!hueLight.isOn()) break;
                    hueController.setLightState(hueLight, false);
                    break;
                default:
                    response.body(getError("Invalid action").toString());
                    return response.body();
            }

            response.body(getSuccess("Hue light updated").toString());
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
