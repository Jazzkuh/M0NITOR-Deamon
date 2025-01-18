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

public class FaderHandler extends GenericHandler {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);
    private final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);

    @Override
    public boolean shouldProcess(byte size, byte cmd) {
        return size == (byte) 0x0A && (cmd == (byte) 0xE2 || cmd == (byte) 0xA2);
    }

    @Override
    public void process(byte size, byte cmd, byte[] data) {
        for (int i = 1; i <= 8; i++) {
            Fader fader = airliteModule.getFaders().get(i);
            boolean active = data[fader.index()] >= 1;

            if (fader.isFaderActive() == active) continue;
            fader.setFaderActive(active);

            if (udpModule.isVoiceTrackEnabled()) {
                udpModule.writeToSocket((byte) 0x03, (byte) 0x0A, active ? (byte) 0x01 : (byte) 0x00);
            }

            ChannelTrigger channelTrigger = new ChannelTrigger(i, active ? TriggerType.FADER_ON : TriggerType.FADER_OFF);
            if (fader.isChannelOn() && active) {
                TriggerAction triggerAction = ChannelTriggerRegistry.getAction(new ChannelTrigger(i, TriggerType.FADER_AND_CHANNEL_ON));
                if (triggerAction != null) {
                    triggerAction.process();
                    udpModule.getLogger().info("Triggered action for channel " + i + ": " + triggerAction.getClass().getSimpleName());
                }
            }

            TriggerAction triggerAction = ChannelTriggerRegistry.getAction(channelTrigger);
            if (triggerAction != null) {
                triggerAction.process();
                udpModule.getLogger().info("Triggered action for channel " + i + ": " + triggerAction.getClass().getSimpleName());
            }
        }
    }
}
