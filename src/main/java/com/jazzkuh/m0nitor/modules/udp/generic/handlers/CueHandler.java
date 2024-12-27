package com.jazzkuh.m0nitor.modules.udp.generic.handlers;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.Fader;
import com.jazzkuh.m0nitor.framework.airlite.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerType;
import com.jazzkuh.m0nitor.framework.udp.GenericHandler;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.airlite.registry.ChannelTriggerRegistry;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;

import java.util.Map;

public class CueHandler extends GenericHandler {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);
    private final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);

    private final Map<Integer, Byte> bits = Map.of(
            1, (byte) 0x01,
            2, (byte) 0x02,
            3, (byte) 0x04,
            4, (byte) 0x08,
            5, (byte) 0x10,
            6, (byte) 0x20,
            7, (byte) 0x40,
            8, (byte) 0x80
    );

    @Override
    public boolean shouldProcess(byte size, byte cmd) {
        return size == (byte) 0x04 && (cmd == (byte) 0xE1 || cmd == (byte) 0xA1);
    }

    @Override
    public void process(byte size, byte cmd, byte[] data) {
        byte data0 = data[4];

        for (int i = 1; i <= 8; i++) {
            byte state = (byte) (data0 & bits.get(i));
            Fader fader = airliteModule.getFaders().get(i);
            fader.setCueActive(state != (byte) 0x00);

            if (!fader.isFaderActive() && fader.isChannelOn()) {
                TriggerType triggerType = state == (byte) 0x00 ? TriggerType.FADER_OFF_CUE_OFF : TriggerType.FADER_OFF_CUE_ON;
                ChannelTrigger channelTrigger = new ChannelTrigger(i, triggerType);
                TriggerAction triggerAction = ChannelTriggerRegistry.getAction(channelTrigger);
                if (triggerAction != null) {
                    triggerAction.process();
                }
                continue;
            }

            TriggerType triggerType = state == (byte) 0x00 ? TriggerType.CUE_OFF : TriggerType.CUE_ON;
            ChannelTrigger channelTrigger = new ChannelTrigger(i, triggerType);

            TriggerAction triggerAction = ChannelTriggerRegistry.getAction(channelTrigger);
            if (triggerAction != null) {
                triggerAction.process();
                udpModule.getLogger().info("Triggered action for channel {}: {}", i, triggerAction.getClass().getSimpleName());
            }
        }

        byte data1 = data[5];
        byte state = (byte) (data1 & (byte) 0x01);
        airliteModule.setCueAux(state == (byte) 0x01);
    }
}
