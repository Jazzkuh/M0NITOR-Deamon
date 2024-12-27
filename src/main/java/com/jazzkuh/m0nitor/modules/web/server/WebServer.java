package com.jazzkuh.m0nitor.modules.web.server;

import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.modules.web.routes.*;
import com.jazzkuh.m0nitor.modules.web.socket.WebSocketHandler;
import spark.Spark;

import java.lang.reflect.InvocationTargetException;

public final class WebServer {
    private final WebModule webModule;

    public WebServer(WebModule webModule, int port) {
        this.webModule = webModule;
        Spark.port(port);
        this.startWebSocket();
        Spark.init();

        this.register(StatusRoute.class);
        this.register(ChannelRoute.class);
        this.register(MediaRoute.class);
        this.register(PFLChannelRoute.class);
        this.register(PFLControlRoute.class);

        Spark.options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }
                    return "OK";
                });

        Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        Spark.get("/", (request, response) -> "<img src=\"https://http.cat/418\"></img>");
        Spark.notFound("<img src=\"https://http.cat/404\"></img>");
        Spark.internalServerError("<img src=\"https://http.cat/500\"></img>");
    }

    private void startWebSocket() {
        Spark.webSocket("/ws", WebSocketHandler.class);
        webModule.getLogger().info("WebSocket has started!");
    }

    private void register(Class<?> clazz) {
        try {
            clazz.getConstructor(WebModule.class).newInstance(this.webModule);
            this.webModule.getLogger().info("Registered route {}", clazz.getSimpleName());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            this.webModule.getLogger().error("Could not register route {}", clazz.getSimpleName(), e);
        }
    }
}