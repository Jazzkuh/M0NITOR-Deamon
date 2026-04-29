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
import lombok.SneakyThrows;

import java.util.Map;

public class RouteHandler extends AuronHandler {
    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    @Override
    public boolean shouldProcess(String msg) {
        return msg.equalsIgnoreCase("on_config_route");
    }

    @Override
    @SneakyThrows
    public void process(String msg, JsonObject param) {
        for (Map.Entry<String, com.google.gson.JsonElement> entry : param.entrySet()) {
            int channelId = Integer.parseInt(entry.getKey().split("_")[1]);
            System.out.println("Processing channel " + channelId + ": " + entry.getValue().getAsJsonObject().toString());

            String bus = entry.getValue().getAsJsonObject().entrySet().iterator().next().getKey();
            boolean value = entry.getValue().getAsJsonObject().get(bus).getAsBoolean();

            Module module = auronModule.getModules().get(channelId);
            switch (bus) {
                case "pgm" -> module.setPgm(value);
                case "sub" -> module.setSub(value);
                default -> System.out.println("Channel " + channelId + " has an unknown routing: " + bus);
            }
        }
    }
}
