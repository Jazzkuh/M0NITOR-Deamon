package com.jazzkuh.m0nitor.modules.udp.tasks;

import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import com.jazzkuh.m0nitor.utils.Concurrency;
import com.jazzkuh.modulemanager.generic.handlers.tasks.TaskInfo;
import lombok.AllArgsConstructor;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.TimerTask;

@AllArgsConstructor
@TaskInfo(period = 4000, delay = 4000, repeating = true)
public class KeepAliveTask extends TimerTask {
    private final UDPModule udpModule;

    @Override
    public void run() {
        byte[] message = new byte[12];
        message[0] = (byte) 0xA0; // Airlite
        message[1] = (byte) 0xA0; // Airlite
        message[2] = (byte) 0x4B; // K
        message[3] = (byte) 0x41; // A

        Concurrency.async().execute(() -> {
            try {
                DatagramPacket sendPacket = new DatagramPacket(message, message.length, InetAddress.getByName(udpModule.getHOST_ADDRESS()), udpModule.getTRANSMIT_PORT());
                udpModule.getReceivingSocket().send(sendPacket);
                udpModule.getLogger().info("Sent keep alive packet to Airlite");
            } catch (Exception e) {
                udpModule.getLogger().warn("Error while sending keep alive packet: {}", e.getMessage());
            }
        });
    }
}
