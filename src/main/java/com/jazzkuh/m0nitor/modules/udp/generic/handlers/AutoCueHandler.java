package com.jazzkuh.m0nitor.modules.udp.generic.handlers;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.udp.GenericHandler;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;

public class AutoCueHandler extends GenericHandler {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);
    private final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);

    @Override
    public boolean shouldProcess(byte size, byte cmd) {
        return size == (byte) 0x04 && (cmd == (byte) 0xA3 || cmd == (byte) 0xE3);
    }

    @Override
    public void process(byte size, byte cmd, byte[] data) {
        byte crm = data[4];
        byte announcer = data[5];

        airliteModule.setAutoCueCrm(crm == (byte) 0x01);
        airliteModule.setAutoCueAnnouncer(announcer == (byte) 0x01);
    }
}
