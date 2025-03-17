package com.jazzkuh.m0nitor.framework.web;

import lombok.Getter;

@Getter
public enum Route {

    STATUS("/status"),
    CHANNEL_STATE("/channel/:channel/:state"),
    PFL_CHANNEL("/pfl/channel/:channel"),
    PFL_CONTROL("/pfl/control/:value"),
    MEDIA("/media/:action"),
    HUE("/hue/action/:action/:light"),
    HUE_BRIGHTNESS("/hue/brightness/:light/:brightness"),
    HUE_SCENE("/hue/scene/:group/:scene"),;

    private final String path;

    Route(String path) {
        this.path = path;
    }

    public String getPath(String... arguments) {
        StringBuilder path = new StringBuilder(this.path);
        for (String argument : arguments) {
            path.append("/").append(argument);
        }

        return path.toString();
    }
}