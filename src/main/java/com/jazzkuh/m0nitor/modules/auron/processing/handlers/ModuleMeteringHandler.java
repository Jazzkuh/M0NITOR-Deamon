package com.jazzkuh.m0nitor.modules.auron.processing.handlers;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.framework.auron.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.modules.auron.registry.ChannelTriggerRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModuleMeteringHandler extends AuronHandler {
    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    @Override
    public boolean shouldProcess(String msg) {
        return msg.startsWith("on_metering:module_");
    }
    @Override
    public void process(String msg, JsonObject param) {
        int channelId = Integer.parseInt(msg.split("on_metering:module_")[1]);
        double left = param.get("left").getAsInt();
        double right = param.get("right").getAsInt();

        Module module = auronModule.getModules().get(channelId);
        module.setLeft(left);
        module.setRight(right);
    }
}
