package com.jazzkuh.m0nitor.modules.auron.processing.handlers;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;
import com.jazzkuh.m0nitor.modules.auron.registry.ChannelTriggerRegistry;

import java.util.Map;

public class MicrophoneHandler extends AuronHandler {
    @Override
    public boolean shouldProcess(String msg) {
        return msg.equalsIgnoreCase("on_on_air");
    }

    @Override
    public void process(String msg, JsonObject param) {
        for (Map.Entry<String, com.google.gson.JsonElement> entry : param.entrySet()) {
            String onAirKey = entry.getKey();
            boolean onAir = entry.getValue().getAsBoolean();

            int channel = Integer.parseInt(onAirKey.replace("on_air", ""));
            if (channel != 1) return;

            ChannelTrigger channelTrigger = new ChannelTrigger(-1, onAir ? TriggerType.MICROPHONE_ON : TriggerType.MICROPHONE_OFF);

            TriggerAction triggerAction = ChannelTriggerRegistry.getAction(channelTrigger);
            if (triggerAction != null) {
                triggerAction.process();
                Deamon.getLogger().info("Triggered action for microphone " + onAirKey + ": " + triggerAction.getClass().getSimpleName());
            }
        }
    }
}
