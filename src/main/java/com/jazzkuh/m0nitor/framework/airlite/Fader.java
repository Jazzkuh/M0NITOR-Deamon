package com.jazzkuh.m0nitor.framework.airlite;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Fader {
    private final int channelId;

    @Setter
    private boolean faderActive;

    @Setter
    private boolean channelOn;

    @Setter
    private boolean cueActive = false;

    private final byte module;

    public Fader(int channelId, byte faderData, byte channelData, byte module) {
        this.channelId = channelId;
        this.faderActive = faderData >= 1;
        this.channelOn = channelData >= 1;
        this.module = module;
    }

    public int index() {
        return channelId + 3;
    }
}
