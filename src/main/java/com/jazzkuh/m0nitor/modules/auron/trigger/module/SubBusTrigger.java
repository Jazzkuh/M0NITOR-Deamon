package com.jazzkuh.m0nitor.modules.auron.trigger.module;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.button.ModuleButton;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import lombok.SneakyThrows;

public class SubBusTrigger extends TriggerAction {

    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);
    private final Module module;
    private final ModuleButton moduleButton;

    public SubBusTrigger(int channel, ModuleButton moduleButton) {
        Module module = auronModule.getModules().get(channel);
        if (module == null) {
            throw new IllegalArgumentException("Module not found for channel: " + channel);
        }

        this.module = module;
        this.moduleButton = moduleButton;
    }

    @Override
	@SneakyThrows
	public void process() {
        if (module.isSub()) {
            EmberLedUtil.writeStaticLed(auronModule, moduleButton, ControlLedColor.OFF);

            JsonObject object = new JsonObject();
            object.addProperty("sub", false);

            JsonObject param = new JsonObject();
            param.add("module_" + module.getChannelId(), object);
            auronModule.sendToSocket("set_config_route", param);
        } else {
            EmberLedUtil.writeStaticLed(auronModule, moduleButton, ControlLedColor.YELLOW);

            JsonObject object = new JsonObject();
            object.addProperty("sub", true);

            JsonObject param = new JsonObject();
            param.add("module_" + module.getChannelId(), object);
            auronModule.sendToSocket("set_config_route", param);
        }
	}

	@Override
	public void startActions() {
        if (module.isSub()) {
            EmberLedUtil.writeStaticLed(auronModule, moduleButton, ControlLedColor.YELLOW);
        } else {
            EmberLedUtil.writeStaticLed(auronModule, moduleButton, ControlLedColor.OFF);
        }
	}
}
