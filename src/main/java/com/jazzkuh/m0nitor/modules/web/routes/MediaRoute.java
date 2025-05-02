package com.jazzkuh.m0nitor.modules.web.routes;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import de.labystudio.spotifyapi.model.MediaKey;

import static spark.Spark.get;

public final class MediaRoute {
    public MediaRoute(WebModule webModule) {
        get(Route.MEDIA.getPath(), (request, response) -> {
            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            String action = request.params(":action");
            MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();
            switch (action.toLowerCase()) {
                case "play":
                    musicEngine.pressMediaKey(MediaKey.PLAY_PAUSE);
                    break;
                case "next":
                    musicEngine.pressMediaKey(MediaKey.NEXT);
                    break;
                case "previous":
                    musicEngine.pressMediaKey(MediaKey.PREV);
                    break;
                default:
                    response.body(getError("Invalid action").toString());
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
