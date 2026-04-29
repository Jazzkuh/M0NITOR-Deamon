package com.jazzkuh.m0nitor.framework.auron.button;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
public enum ModuleButton {
    // TOP LEFT: 0, TOP RIGHT: 1, MIDDLE: 2, BOTTOM: 3
    // 0 AURON, 1 MODULES, MODULE 0 - 9, 5 KEYBOARD CONTROL, BUTTON SEE ABOVE, 0 STATE - 1 BLINK - 2 ON COLOR - 3 OFF COLOR
    MOD_1_TOP_LEFT(1, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_2_TOP_LEFT(2, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_3_TOP_LEFT(3, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_4_TOP_LEFT(4, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_5_TOP_LEFT(5, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_6_TOP_LEFT(6, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_7_TOP_LEFT(7, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_8_TOP_LEFT(8, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_9_TOP_LEFT(9, "sw_1", 0, ControlLedColor.YELLOW, false),
    MOD_10_TOP_LEFT(10, "sw_1", 0, ControlLedColor.YELLOW, false);

    private static final String EMBER_BASE_PATH = "0.1";

    private final int module;
    private final String switchKey;
    private final int emberIndex;
    private final ControlLedColor defaultLedColor;
    private final boolean hasPressedColor;

    ModuleButton(int module, String switchKey, int emberIndex, ControlLedColor defaultLedColor) {
        this.module = module;
        this.switchKey = switchKey;
        this.emberIndex = emberIndex;
        this.defaultLedColor = defaultLedColor;
        this.hasPressedColor = true;
    }

    ModuleButton(int module, String switchKey, int emberIndex, ControlLedColor defaultLedColor, boolean hasPressedColor) {
        this.module = module;
        this.switchKey = switchKey;
        this.emberIndex = emberIndex;
        this.defaultLedColor = defaultLedColor;
        this.hasPressedColor = hasPressedColor;
    }

    @Nullable
    public static ModuleButton by(int module, String switchKey) {
        for (ModuleButton button : values()) {
            if (button.module == module && button.switchKey.equals(switchKey)) {
                return button;
            }
        }

        return null;
    }

    /**
     * Ember+ path to the State parameter (boolean).
     * e.g. "0.2.4.0.0" for LED_1A
     */
    public String getEmberStatePath() {
        return  EMBER_BASE_PATH + "." + (module - 1) + ".5." + emberIndex + ".0";
    }

    /**
     * Ember+ path to the Blink mode parameter (enum 0-3).
     * e.g. "0.2.4.0.1" for LED_1A
     */
    public String getEmberBlinkModePath() {
        return EMBER_BASE_PATH + "." + (module - 1) + ".5." + emberIndex + ".1";
    }

    /**
     * Ember+ path to the On color parameter (enum 0-3).
     * e.g. "0.2.4.0.2" for LED_1A
     */
    public String getEmberOnColorPath() {
        return EMBER_BASE_PATH + "." + (module - 1) + ".5." + emberIndex + ".2";
    }

    /**
     * Ember+ path to the Off color parameter (enum 0-3).
     * e.g. "0.2.4.0.3" for LED_1A
     */
    public String getEmberOffColorPath() {
        return EMBER_BASE_PATH + "." + (module - 1) + ".5." + emberIndex + ".3";
    }
}