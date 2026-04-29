package com.jazzkuh.m0nitor.framework.auron;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Module {
    private final int channelId;

    @Setter
    private String name;

    @Setter
    private boolean active;

    @Setter
    private int faderLevel;

    @Setter
    private boolean cue;

    @Setter
    private double left;

    @Setter
    private double right;

    @Setter
    private boolean pgm;

    @Setter
    private boolean sub;

    public Module(int channelId, boolean active, int faderLevel, boolean cue) {
        this.channelId = channelId;
        this.active = active;
        this.faderLevel = faderLevel;
        this.cue = cue;
        this.name = "Channel " + channelId;
        this.pgm = true;
        this.sub = true;
    }

    public boolean isFaderActive() {
        return faderLevel >= 10;
    }
}
