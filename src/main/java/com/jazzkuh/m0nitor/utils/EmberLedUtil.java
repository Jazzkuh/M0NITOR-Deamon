package com.jazzkuh.m0nitor.utils;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import lombok.experimental.UtilityClass;

/**
 * Utility for controlling Auron master control LEDs via Ember+ set_value
 * commands sent through the Auron WebSocket API.
 *
 * <p>Ember+ tree layout for keyboard control:</p>
 * <pre>
 *   0.2.4.{keyIndex}.0  →  State      (boolean)
 *   0.2.4.{keyIndex}.1  →  Blink mode (enum: 0=Static, 1=Slow, 2=Normal, 3=Fast)
 *   0.2.4.{keyIndex}.2  →  On color   (enum: 0=Off, 1=Red, 2=Green, 3=Yellow)
 *   0.2.4.{keyIndex}.3  →  Off color  (enum: 0=Off, 1=Red, 2=Green, 3=Yellow)
 * </pre>
 *
 * <p>Usage:</p>
 * <pre>
 *   EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_7A, ControlLedColor.GREEN);
 *   EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
 *   EmberLedUtil.writeAll(auronModule, ControlLedColor.OFF);
 * </pre>
 */
@UtilityClass
public class EmberLedUtil {
    public static void writeStaticLed(AuronModule module, ControlButton button, ControlLedColor color) {
        sendSetValue(module, button.getEmberOnColorPath(), color.getEmberValue());
        sendSetValue(module, button.getEmberBlinkModePath(), ControlLedBlinkSpeed.STATIC.getEmberValue());
    }

    /**
     * Sets a blinking LED with two alternating colors and a blink speed.
     */
    public static void writeBlinkingLed(AuronModule module, ControlButton button,
                                        ControlLedColor onColor, ControlLedColor offColor,
                                        ControlLedBlinkSpeed speed) {
        sendSetValue(module, button.getEmberOnColorPath(), onColor.getEmberValue());
        sendSetValue(module, button.getEmberOffColorPath(), offColor.getEmberValue());
        sendSetValue(module, button.getEmberBlinkModePath(), speed.getEmberValue());
    }

    /**
     * Sets all A and B row buttons to a single static color.
     */
    public static void writeAll(AuronModule module, ControlLedColor color) {
        for (ControlButton button : ControlButton.values()) {
            writeStaticLed(module, button, color);
        }
    }

    /**
     * Sets all buttons in a specific row to a single static color.
     *
     * @param row "A" or "B"
     */
    public static void writeRow(AuronModule module, String row, ControlLedColor color) {
        for (ControlButton button : ControlButton.values()) {
            if (button.getRow().equals(row)) {
                writeStaticLed(module, button, color);
            }
        }
    }

    /**
     * Turns off a single LED.
     */
    public static void writeLedOff(AuronModule module, ControlButton button) {
        writeStaticLed(module, button, ControlLedColor.OFF);
    }

    /**
     * Turns off all LEDs on rows A and B.
     */
    public static void writeAllOff(AuronModule module) {
        writeAll(module, ControlLedColor.OFF);
    }

    /**
     * Reads the current on_color value of a button via Ember+ get_value.
     */
    public static void readOnColor(AuronModule module, ControlButton button) {
        sendGetValue(module, button.getEmberOnColorPath());
    }

    /**
     * Reads the current blink mode of a button via Ember+ get_value.
     */
    public static void readBlinkMode(AuronModule module, ControlButton button) {
        sendGetValue(module, button.getEmberBlinkModePath());
    }

    // ── Internal ──

    private static void sendSetValue(AuronModule module, String path, int value) {
        JsonObject param = new JsonObject();
        param.addProperty("path", path);
        param.addProperty("value", value);
        module.sendToSocket("set_value", param);
    }

    private static void sendGetValue(AuronModule module, String path) {
        JsonObject param = new JsonObject();
        param.addProperty("path", path);
        module.sendToSocket("get_value", param);
    }
}