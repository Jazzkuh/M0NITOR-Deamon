package com.jazzkuh.m0nitor.framework.auron.button;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
public enum ControlButton {
    LED_1A("A", "a_1", 0, ControlLedColor.GREEN, false),
    LED_2A("A", "a_2", 1, ControlLedColor.GREEN),
    LED_3A("A", "a_3", 2, ControlLedColor.GREEN),
    LED_4A("A", "a_4", 3, ControlLedColor.GREEN, false),
    LED_5A("A", "a_5", 4, ControlLedColor.GREEN, false),
    LED_6A("A", "a_6", 5, ControlLedColor.GREEN, false),
    LED_7A("A", "a_7", 6, ControlLedColor.GREEN, false),
    LED_8A("A", "a_8", 7, ControlLedColor.GREEN, false),
    LED_1B("B", "b_1", 8, ControlLedColor.YELLOW, false),
    LED_2B("B", "b_2", 9, ControlLedColor.YELLOW, false),
    LED_3B("B", "b_3", 10, ControlLedColor.YELLOW, false),
    LED_4B("B", "b_4", 11, ControlLedColor.YELLOW, false),
    LED_5B("B", "b_5", 12, ControlLedColor.YELLOW, false),
    LED_6B("B", "b_6", 13, ControlLedColor.YELLOW, false),
    LED_7B("B", "b_7", 14, ControlLedColor.GREEN, false),
    LED_8B("B", "b_8", 15, ControlLedColor.GREEN, false);

    private static final String EMBER_BASE_PATH = "0.2.4";

    private final String row;
    private final String switchKey;
    private final int emberIndex;
    private final ControlLedColor defaultLedColor;
    private final boolean hasPressedColor;

    ControlButton(String row, String switchKey, int emberIndex, ControlLedColor defaultLedColor) {
        this.row = row;
        this.switchKey = switchKey;
        this.emberIndex = emberIndex;
        this.defaultLedColor = defaultLedColor;
        this.hasPressedColor = true;
    }

    ControlButton(String row, String switchKey, int emberIndex, ControlLedColor defaultLedColor, boolean hasPressedColor) {
        this.row = row;
        this.switchKey = switchKey;
        this.emberIndex = emberIndex;
        this.defaultLedColor = defaultLedColor;
        this.hasPressedColor = hasPressedColor;
    }

    /**
     * Ember+ path to the State parameter (boolean).
     * e.g. "0.2.4.0.0" for LED_1A
     */
    public String getEmberStatePath() {
        return EMBER_BASE_PATH + "." + emberIndex + ".0";
    }

    /**
     * Ember+ path to the Blink mode parameter (enum 0-3).
     * e.g. "0.2.4.0.1" for LED_1A
     */
    public String getEmberBlinkModePath() {
        return EMBER_BASE_PATH + "." + emberIndex + ".1";
    }

    /**
     * Ember+ path to the On color parameter (enum 0-3).
     * e.g. "0.2.4.0.2" for LED_1A
     */
    public String getEmberOnColorPath() {
        return EMBER_BASE_PATH + "." + emberIndex + ".2";
    }

    /**
     * Ember+ path to the Off color parameter (enum 0-3).
     * e.g. "0.2.4.0.3" for LED_1A
     */
    public String getEmberOffColorPath() {
        return EMBER_BASE_PATH + "." + emberIndex + ".3";
    }
}