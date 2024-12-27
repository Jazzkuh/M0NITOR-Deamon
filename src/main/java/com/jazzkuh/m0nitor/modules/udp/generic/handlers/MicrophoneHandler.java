package com.jazzkuh.m0nitor.modules.udp.generic.handlers;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerType;
import com.jazzkuh.m0nitor.framework.udp.GenericHandler;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.airlite.registry.ChannelTriggerRegistry;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;

public class MicrophoneHandler extends GenericHandler {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);
    private final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);

    @Override
    public boolean shouldProcess(byte size, byte cmd) {
        return size == (byte) 0x03 && cmd == (byte) 0xE6;
    }

    @Override
    public void process(byte size, byte cmd, byte[] data) {
        byte state = data[4];
        ChannelTrigger channelTrigger = new ChannelTrigger(-1, state == (byte) 0x00 ? TriggerType.MICROPHONE_OFF : TriggerType.MICROPHONE_ON);

        TriggerAction triggerAction = ChannelTriggerRegistry.getAction(channelTrigger);
        if (triggerAction != null) {
            triggerAction.process();
        }
    }
}
