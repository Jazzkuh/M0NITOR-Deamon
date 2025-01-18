package com.jazzkuh.m0nitor.modules.web.routes;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import io.github.zeroone3010.yahueapi.v2.Light;

import static spark.Spark.get;

public final class HueBrightnessRoute {
    private final HueController hueController = Deamon.getInstance().getHueController();

    public HueBrightnessRoute(WebModule webModule) {
        get(Route.HUE_BRIGHTNESS.getPath(), (request, response) -> {
            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            String light = request.params(":light");
            String brightness = request.params(":brightness");

            Light hueLight = hueController.getLightByName(light);
            if (hueLight == null) {
                response.body(getError("Light not found").toString());
                return response.body();
            }

            hueController.setLightBrightness(hueLight, Integer.parseInt(brightness));

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
