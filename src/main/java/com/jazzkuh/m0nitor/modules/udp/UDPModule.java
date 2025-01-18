package com.jazzkuh.m0nitor.modules.udp;

import com.jazzkuh.m0nitor.framework.airlite.Fader;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.udp.generic.GenericListener;
import com.jazzkuh.m0nitor.modules.udp.metering.MeteringListener;
import com.jazzkuh.m0nitor.modules.udp.tasks.KeepAliveTask;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.utils.Concurrency;
import com.jazzkuh.m0nitor.utils.lighting.PacketRunnable;
import com.jazzkuh.modulemanager.generic.GenericModule;
import com.jazzkuh.modulemanager.generic.GenericModuleManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

@Getter
public class UDPModule extends GenericModule {
    private DatagramSocket receivingSocket;
    private DatagramSocket meteringSocket;

    private final int RECEIVING_PORT = 19549;
    private final int METERING_PORT = 19548;
    private final int TRANSMIT_PORT = 19551;

    private final String HOST_ADDRESS = "127.0.0.1";

    @Getter @Setter
    private Map<String, Double> meteringValues = new HashMap<>();

    @Getter @Setter
    private boolean voiceTrackEnabled = false;

    private WebModule webModule;

    public UDPModule(GenericModuleManager owningManager, WebModule webModule, AirliteModule airliteModule) {
        super(owningManager);
    }

    @Override
    @SneakyThrows
    public void onLoad() {
        receivingSocket = new DatagramSocket(RECEIVING_PORT);
        this.getLogger().info("UDP server is running on port " + RECEIVING_PORT);

        meteringSocket = new DatagramSocket(METERING_PORT);
        this.getLogger().info("UDP metering server is running on port " + METERING_PORT);
    }

    @Override
    @SneakyThrows
    public void onEnable() {
        this.webModule = getOwningManager().get(WebModule.class);
        GenericListener genericListener = new GenericListener(this, webModule, receivingSocket);
        genericListener.start();

        MeteringListener meteringListener = new MeteringListener(this, webModule, meteringSocket);
        meteringListener.start();

        KeepAliveTask keepAliveTask = new KeepAliveTask(this);
        keepAliveTask.run();

        registerComponent(keepAliveTask);
        registerComponent(new PacketRunnable());

        // Request current cue states
        this.writeToSocket((byte) 0x02, (byte) 0x63);
        this.writeToSocket((byte) 0x02, (byte) 0x61);

        // Request the current state of the faders
        this.writeToSocket((byte) 0x04, (byte) 0x62);
        this.writeToSocket((byte) 0x04, (byte) 0x60);
    }

    @Override
    public void onDisable() {
    }

    public void writeToSocket(byte size, byte cmd, byte... data) {
        Concurrency.async().execute(() -> {
            byte[] message = new byte[12];
            message[0] = (byte) 0xA0; // Airlite
            message[1] = (byte) 0xA0; // Airlite
            message[2] = size; // Size
            message[3] = cmd; // CMD

            if (data.length > 0) {
                System.arraycopy(data, 0, message, 4, data.length);
            }

            try {
                DatagramPacket sendPacket = new DatagramPacket(message, message.length, InetAddress.getByName(HOST_ADDRESS), TRANSMIT_PORT);
                receivingSocket.send(sendPacket);
            } catch (Exception e) {
                this.getLogger().warn("Error while sending data: {}", e.getMessage());
            }
        });
    }

    public void writeStaticLed(ControlButton controlButton, ControlLedColor ledColor) {
        this.writeToSocket((byte) 0x04, (byte) 0x02, controlButton.getButtonId(), ledColor.getData());
    }

    public void writeBlinkingLed(ControlButton controlButton, ControlLedColor colorOn, ControlLedColor colorOff, ControlLedBlinkSpeed blinkSpeed) {
        this.writeToSocket((byte) 0x04, (byte) 0x03, controlButton.getButtonId(), colorOn.getData(), colorOff.getData(), blinkSpeed.getData());
    }

    public void writeRemoteOn(Fader fader, boolean activate) {
        this.writeToSocket((byte) 0x04, (byte) 0x05, fader.getModule(), (byte) (activate ? 0x01 : 0x00));
    }
}
