package com.jazzkuh.m0nitor.modules.web.routes;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import io.github.zeroone3010.yahueapi.v2.Group;

import static spark.Spark.get;

public final class HueSceneRoute {
    private final HueController hueController = Deamon.getInstance().getHueController();

    public HueSceneRoute(WebModule webModule) {
        get(Route.HUE_SCENE.getPath(), (request, response) -> {
            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            String room = request.params(":group");
            String scene = request.params(":scene");

            Group group = hueController.getRoomByName(room);
            if (group == null) {
                response.body(getError("Room not found").toString());
                return response.body();
            }

            hueController.setScene(group, scene);
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
