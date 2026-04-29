package com.jazzkuh.m0nitor.modules.auron.processing.handlers;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.framework.auron.button.ModuleButton;
import com.jazzkuh.m0nitor.framework.auron.button.ModuleButtonTrigger;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;
import com.jazzkuh.m0nitor.modules.auron.registry.ModuleButtonTriggerRegistry;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InitialStatesHandler extends AuronHandler {
    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    @Override
    public boolean shouldProcess(String msg) {
        return msg.equalsIgnoreCase("all");
    }

    @Override
    public void process(String msg, JsonObject param) {
        for (Map.Entry<String, com.google.gson.JsonElement> entry : param.entrySet()) {
            if (!entry.getKey().startsWith("module_")) continue;
            int channelId = Integer.parseInt(entry.getKey().split("_")[1]);
            JsonObject data = entry.getValue().getAsJsonObject();

            Module module = auronModule.getModules().get(channelId);
            module.setFaderLevel(data.get("level").getAsInt());
            module.setActive(data.get("on").getAsBoolean());
            module.setName(data.get("config_source_name").getAsString());

            JsonObject routes = data.get("config_route").getAsJsonObject();
            module.setPgm(routes.get("pgm").getAsBoolean());
            module.setSub(routes.get("sub").getAsBoolean());
        }


        for (ModuleButtonTrigger moduleButtonTrigger : ModuleButtonTriggerRegistry.getTriggers().keySet()) {
            ModuleButton moduleButton = moduleButtonTrigger.getModuleButton();
            EmberLedUtil.writeStaticLed(auronModule, moduleButton, moduleButton.getDefaultLedColor());

            TriggerAction triggerAction = ModuleButtonTriggerRegistry.getAction(moduleButtonTrigger);
            if (triggerAction == null) continue;
            triggerAction.startActions();
        }
    }
}
