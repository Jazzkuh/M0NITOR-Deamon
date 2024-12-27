package com.jazzkuh.m0nitor.modules.udp.generic;

import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Getter
@AllArgsConstructor
public class GenericListener extends Thread {
    private final UDPModule udpModule;
    private final WebModule webModule;
    private final DatagramSocket socket;

    private final byte[] receiveData = new byte[12];
    private final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

    @SneakyThrows
    public void run() {
        while (true) {
            socket.receive(receivePacket);
            webModule.sendToSocket(webModule.getJson());

            GenericProcessor genericProcessor = new GenericProcessor(udpModule);
            genericProcessor.process(receivePacket.getData());
        }
    }
}