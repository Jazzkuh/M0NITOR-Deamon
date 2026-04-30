package com.jazzkuh.m0nitor.modules.auron.processing.handlers;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.framework.auron.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;
import com.jazzkuh.m0nitor.modules.auron.registry.ChannelTriggerRegistry;

import java.util.Map;

public class CueHandler extends AuronHandler {
    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    @Override
    public boolean shouldProcess(String msg) {
        return msg.equalsIgnoreCase("on_cue");
    }

    @Override
    public void process(String msg, JsonObject param) {
        for (Map.Entry<String, com.google.gson.JsonElement> entry : param.entrySet()) {
            int channelId = Integer.parseInt(entry.getKey().split("_")[1]);
            boolean active = entry.getValue().getAsBoolean();

            Module module = auronModule.getModules().get(channelId);
            module.setCue(active);

            Deamon.getLogger().info("Fader: " + channelId + ", Cue on: " + active);

            boolean faderActive = module.isFaderActive();
            TriggerType triggerType = active ? (faderActive ? TriggerType.CUE_ON : TriggerType.FADER_OFF_CUE_ON) : (faderActive ? TriggerType.CUE_OFF : TriggerType.FADER_OFF_CUE_OFF);
            ChannelTrigger channelTrigger = new ChannelTrigger(channelId, triggerType);

            TriggerAction triggerAction = ChannelTriggerRegistry.getAction(channelTrigger);
            if (triggerAction != null) {
                triggerAction.process();
                Deamon.getLogger().info("Triggered action for channel " + channelId + ": " + triggerAction.getClass().getSimpleName());
            }
        }
    }
}
