package com.jazzkuh.m0nitor.modules.auron.processing.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.*;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.modules.auron.registry.ButtonTriggerRegistry;
import com.jazzkuh.m0nitor.modules.auron.registry.ModuleButtonTriggerRegistry;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;

import java.util.Map;

public class ButtonHandler extends AuronHandler {
    private AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    @Override
    public boolean shouldProcess(String msg) {
        return msg.equalsIgnoreCase("on_ctrl_key");
    }

    @Override
    public void process(String msg, JsonObject param) {
        for (Map.Entry<String, com.google.gson.JsonElement> entry : param.entrySet()) {
            String key = entry.getKey();
            if (key == null) continue;
            if (key.startsWith("module_")) {
                JsonElement value = entry.getValue();
                String module = key.substring("module_".length());
                String switchKey = value.getAsJsonObject().keySet().iterator().next();

                ModuleButton moduleButton = ModuleButton.by(Integer.parseInt(module), switchKey);
                if (moduleButton == null) {
                    System.out.println("Unknown module button: " + key);
                    continue;
                }

                boolean pressed = value.getAsJsonObject().get(switchKey).getAsBoolean();

                if (moduleButton.isHasPressedColor()) {
                    if (pressed) {
                        EmberLedUtil.writeStaticLed(auronModule, moduleButton, ControlLedColor.RED);
                    } else {
                        EmberLedUtil.writeStaticLed(auronModule, moduleButton, moduleButton.getDefaultLedColor());
                    }
                }

                System.out.println("Received module state change: " + key + " = " + value + ", Mapped Key: " + switchKey);

                ModuleButtonTrigger moduleButtonTrigger = new ModuleButtonTrigger(moduleButton, pressed ? TriggerType.MODULE_BUTTON_PRESSED : TriggerType.MODULE_BUTTON_RELEASED);
                TriggerAction triggerAction = ModuleButtonTriggerRegistry.getAction(moduleButtonTrigger);

                if (triggerAction != null) {
                    triggerAction.process();
                    System.out.println("Triggered action for module button " + key + ": " + triggerAction.getClass().getSimpleName());
                }

                continue;
            }

            if (!key.startsWith("a_") && !key.startsWith("b_")) continue;
            ControlButton controlButton = ControlButton.valueOf("LED_" + Integer.parseInt(key.split("_")[1]) + (key.startsWith("a_") ? "A" : "B"));
            boolean pressed = entry.getValue().getAsBoolean();

            if (controlButton.isHasPressedColor()) {
                if (pressed) {
                    EmberLedUtil.writeStaticLed(auronModule, controlButton, ControlLedColor.RED);
                } else {
                    EmberLedUtil.writeStaticLed(auronModule, controlButton, controlButton.getDefaultLedColor());
                }
            }

            System.out.println("Button: " + key + ", Pressed: " + pressed + ", Mapped Key: " + controlButton);

            ButtonTrigger buttonTrigger = new ButtonTrigger(controlButton, pressed ? TriggerType.BUTTON_PRESSED : TriggerType.BUTTON_RELEASED);
            TriggerAction triggerAction = ButtonTriggerRegistry.getAction(buttonTrigger);

            if (triggerAction != null) {
                triggerAction.process();
                System.out.println("Triggered action for button " + key + ": " + triggerAction.getClass().getSimpleName());
            }
        }
    }
}
