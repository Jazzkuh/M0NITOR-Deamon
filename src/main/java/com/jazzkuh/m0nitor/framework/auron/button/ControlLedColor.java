package com.jazzkuh.m0nitor.framework.auron.button;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
public enum ControlLedColor {
    OFF(0),
    RED(1),
    GREEN(2),
    YELLOW(3);

    private final int emberValue;

    @Nullable
    public static ControlLedColor fromEmberValue(int value) {
        for (ControlLedColor color : values()) {
            if (color.getEmberValue() == value) {
                return color;
            }
        }
        return null;
    }
}