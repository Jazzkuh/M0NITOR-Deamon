package com.jazzkuh.m0nitor.modules.omniplayer.gpio;

import lombok.experimental.UtilityClass;

/**
 * Utility for controlling OmniPlayer GPI contacts via the TCP GPIO plugin.
 *
 * <p>The plugin exposes 64 GPI contacts (1..64), each addressable by number.
 * Contacts are driven via a persistent TCP connection where the plugin acts
 * as the TCP server and this client sends CONTACT / WRITE commands.</p>
 *
 * <p>Protocol commands sent to the plugin:</p>
 * <pre>
 *   CONTACT &lt;n&gt; ON              →  Set contact n to ON
 *   CONTACT &lt;n&gt; OFF             →  Set contact n to OFF
 *   CONTACT &lt;n&gt; PULSE &lt;ms&gt;     →  Pulse contact n ON for &lt;ms&gt; milliseconds
 *   WRITE &lt;bitmask&gt;             →  Set all 64 contacts at once via 64-bit mask
 *   GET_INPUT_CONTACTS          →  Query current input bitmask (response: 200 &lt;long&gt;)
 *   GET_OUTPUT_CONTACTS         →  Query current output bitmask (response: 200 &lt;long&gt;)
 * </pre>
 *
 * <p>Usage:</p>
 * <pre>
 *   GpiUtil.setContact(gpioModule, GpiContact.MIC_OPEN, true);
 *   GpiUtil.pulseContact(gpioModule, GpiContact.ON_AIR, 500);
 *   GpiUtil.setAllOff(gpioModule);
 *   GpiUtil.setRow(gpioModule, GpiContact.Row.STUDIO_A, false);
 * </pre>
 */
@UtilityClass
public class GpiUtils {

    /**
     * Sets a single contact ON or OFF.
     *
     * @param module  the active GPIO module holding the TCP connection
     * @param contact the target contact (1..64)
     * @param on      true = ON, false = OFF
     */
    public static void setContact(GpioModule module, int contact, boolean on) {
        sendContact(module, contact, on ? "ON" : "OFF");
    }

    /**
     * Turns a single contact ON.
     */
    public static void setContactOn(GpioModule module, int contact) {
        sendContact(module, contact, "ON");
    }

    /**
     * Turns a single contact OFF.
     */
    public static void setContactOff(GpioModule module, int contact) {
        sendContact(module, contact, "OFF");
    }

    /**
     * Pulses a contact ON for the specified duration, then back OFF.
     *
     * @param module      the active GPIO module
     * @param contact     the target contact (1..64)
     */
    public static void pulseContact(GpioModule module, int contact) {
        sendRaw(module, "CONTACT " + contact + " PULSE " + 3);
    }

    /**
     * Writes all 64 contacts at once via a 64-bit bitmask.
     * Bit 0 (LSB) = contact 1, bit 63 = contact 64.
     *
     * @param module  the active GPIO module
     * @param mask    64-bit bitmask representing all contact states
     */
    public static void writeMask(GpioModule module, long mask) {
        sendRaw(module, "WRITE " + mask);
    }

    /**
     * Sets all 64 contacts OFF.
     */
    public static void setAllOff(GpioModule module) {
        writeMask(module, 0L);
    }

    /**
     * Sets all 64 contacts ON.
     */
    public static void setAllOn(GpioModule module) {
        writeMask(module, 0xFFFFFFFFFFFFFFFFL);
    }

    /**
     * Sets a range of contacts to the given state.
     *
     * @param module   the active GPIO module
     * @param from     first contact in range (1..64, inclusive)
     * @param to       last contact in range (1..64, inclusive)
     * @param on       true = ON, false = OFF
     */
    public static void setRange(GpioModule module, int from, int to, boolean on) {
        for (int i = from; i <= to; i++) {
            setContact(module, i, on);
        }
    }

    /**
     * Requests the current GPI input bitmask from the plugin.
     * The plugin responds asynchronously with "200 &lt;long&gt;".
     */
    public static void queryInputContacts(GpioModule module) {
        sendRaw(module, "GET_INPUT_CONTACTS");
    }

    /**
     * Requests the current GPO output bitmask from the plugin.
     * The plugin responds asynchronously with "200 &lt;long&gt;".
     */
    public static void queryOutputContacts(GpioModule module) {
        sendRaw(module, "GET_OUTPUT_CONTACTS");
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private static void sendContact(GpioModule module, int contact, String state) {
        if (contact < 1 || contact > 64) {
            throw new IllegalArgumentException("Contact must be 1..64, got: " + contact);
        }
        sendRaw(module, "CONTACT " + contact + " " + state);
    }

    private static void sendRaw(GpioModule module, String command) {
        module.sendToPlugin(command);
    }
}