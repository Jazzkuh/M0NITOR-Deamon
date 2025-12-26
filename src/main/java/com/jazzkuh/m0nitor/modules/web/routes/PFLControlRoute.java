package com.jazzkuh.m0nitor.modules.web.routes;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.web.WebModule;

import static spark.Spark.get;

public final class PFLControlRoute {
    public PFLControlRoute(WebModule webModule) {
        get(Route.PFL_CONTROL.getPath(), (request, response) -> {
            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            String value = request.params(":value");
            switch (value.toLowerCase()) {
                case "reset":
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
