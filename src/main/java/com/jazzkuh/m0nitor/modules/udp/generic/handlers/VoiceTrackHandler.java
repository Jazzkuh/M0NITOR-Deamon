package com.jazzkuh.m0nitor.modules.udp.generic.handlers;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.udp.GenericHandler;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;

public class VoiceTrackHandler extends GenericHandler {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);
    private final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);

    @Override
    public boolean shouldProcess(byte size, byte cmd) {
        return size == (byte) 0x0A && (cmd == (byte) 0xCD || cmd == (byte) 0x8D);
    }

    @Override
    public void process(byte size, byte cmd, byte[] data) {
        boolean state = data[4] == 0x01;
        if (!state) {
            udpModule.writeToSocket((byte) 0x03, (byte) 0x0A, (byte) 0x00);
        }

        if (airliteModule.getFaders().get(1).isFaderActive()) {
            udpModule.writeToSocket((byte) 0x03, (byte) 0x0A, state ? (byte) 0x01 : (byte) 0x00);
        }

        udpModule.setVoiceTrackEnabled(state);
    }
}
