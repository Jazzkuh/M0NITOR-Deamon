package com.jazzkuh.m0nitor.modules.web.routes;

import com.jazzkuh.m0nitor.framework.web.Route;
import com.jazzkuh.m0nitor.modules.web.WebModule;

import static spark.Spark.get;

public final class StatusRoute {
    public StatusRoute(WebModule webModule) {
        get(Route.STATUS.getPath(), (request, response) -> {
            response.type("application/json");
            response.status(200);

            if (!webModule.authorized(request, response)) {
                return response.body();
            }

            response.body(webModule.getJson().toString());
            return response.body();
        });
    }
}
