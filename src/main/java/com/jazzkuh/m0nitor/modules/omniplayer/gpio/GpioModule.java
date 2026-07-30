package com.jazzkuh.m0nitor.modules.omniplayer.gpio;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Module that manages the persistent TCP connection to OmniPlayer's TCP GPIO plugin.
 *
 * <p>Architecture (from TcpGpio.dll reverse engineering):</p>
 * <ul>
 *   <li><b>GPI (input)</b>: The plugin is the TCP server. This module connects to it
 *       and sends CONTACT / WRITE commands.</li>
 *   <li><b>GPO (output)</b>: The plugin is the TCP client. It connects to an external
 *       server (not this module) to poll GET_OUTPUT_CONTACTS.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 *   GpioModule gpio = new GpioModule("127.0.0.1", 4740);
 *   gpio.setResponseHandler(response -> log.info("Plugin: {}", response));
 *   gpio.connect();
 *
 *   GpiUtil.setContact(gpio, 3, true);
 *   GpiUtil.pulseContact(gpio, 1, 500);
 * </pre>
 */
@Slf4j
public class GpioModule {

    private final String host;
    private final int port;

    @Getter
    private boolean connected = false;

    private Socket socket;
    private PrintWriter writer;
    /**
     * -- SETTER --
     *  Sets a handler called for every response line received from the plugin.
     *  Useful for reading GET_INPUT_CONTACTS / GET_OUTPUT_CONTACTS replies.
     */
    @Setter
    private Consumer<String> responseHandler;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "gpio-reconnect");
        t.setDaemon(true);
        return t;
    });

    public GpioModule(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Starts the connection loop. Reconnects automatically every 5 seconds on failure.
     */
    public void connect() {
        scheduler.execute(this::connectLoop);
    }

    /**
     * Disconnects and shuts down the reconnect loop.
     */
    public void disconnect() {
        scheduler.shutdownNow();
        closeSocket();
    }

    /**
     * Sends a raw TCP GPIO command to the plugin.
     */
    public void sendToPlugin(String command) {
        PrintWriter w = writer;
        if (w == null || !connected) {
            log.warn("GPIO not connected — cannot send: {}", command);
            return;
        }
        w.print(command + "\r\n");
        w.flush();
        log.debug("GPIO >> {}", command);
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void connectLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                log.info("GPIO connecting to {}:{}", host, port);
                socket = new Socket(host, port);
                socket.setKeepAlive(true);
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), false);

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Plugin sends greeting on connect
                String greeting = reader.readLine();
                connected = true;
                log.info("GPIO connected. Plugin: {}", greeting);

                // Drain responses from plugin (200 OK for each command, or bitmask replies)
                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug("GPIO << {}", line);
                    if (responseHandler != null) responseHandler.accept(line);
                }

            } catch (IOException e) {
                if (!Thread.currentThread().isInterrupted()) {
                    log.warn("GPIO connection lost ({}), retrying in 5s...", e.getMessage());
                }
            } finally {
                connected = false;
                writer = null;
                closeSocket();
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void closeSocket() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
        socket = null;
    }
}