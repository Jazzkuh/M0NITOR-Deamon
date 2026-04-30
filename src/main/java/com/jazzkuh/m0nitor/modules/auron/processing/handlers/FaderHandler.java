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

public class FaderHandler extends AuronHandler {
    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    @Override
    public boolean shouldProcess(String msg) {
        return msg.equalsIgnoreCase("on_fader");
    }

    @Override
    public void process(String msg, JsonObject param) {
        for (Map.Entry<String, com.google.gson.JsonElement> entry : param.entrySet()) {
            int channelId = Integer.parseInt(entry.getKey().split("_")[1]);
            int level = entry.getValue().getAsInt();

            Module module = auronModule.getModules().get(channelId);
            module.setFaderLevel(level);

            ChannelTrigger channelTrigger = new ChannelTrigger(channelId, module.isFaderActive() ? TriggerType.FADER_ON : TriggerType.FADER_OFF);
            TriggerAction triggerAction = ChannelTriggerRegistry.getAction(channelTrigger);
            if (triggerAction != null) {
                triggerAction.process();
                Deamon.getLogger().info("Triggered action for channel " + channelId + ": " + triggerAction.getClass().getSimpleName());
            }
        }
    }
}
