package com.jazzkuh.m0nitor.modules.auron.processing.handlers;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;

import java.util.Map;

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
        }
    }
}
