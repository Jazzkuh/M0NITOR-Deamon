package com.jazzkuh.m0nitor.framework.auron.button;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
public enum ControlLedBlinkSpeed {
    STATIC(0),
    SLOW(1),
    NORMAL(2),
    FAST(3);

    private final int emberValue;

    @Nullable
    public static ControlLedBlinkSpeed fromEmberValue(int value) {
        for (ControlLedBlinkSpeed speed : values()) {
            if (speed.getEmberValue() == value) {
                return speed;
            }
        }
        return null;
    }
}