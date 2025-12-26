package com.jazzkuh.m0nitor.modules.auron.processing.handlers;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.framework.auron.button.ButtonTrigger;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;
import com.jazzkuh.m0nitor.modules.auron.registry.ButtonTriggerRegistry;

import java.util.Map;

public class ButtonHandler extends AuronHandler {

    @Override
    public boolean shouldProcess(String msg) {
        return msg.equalsIgnoreCase("on_ctrl_key");
    }

    @Override
    public void process(String msg, JsonObject param) {
        for (Map.Entry<String, com.google.gson.JsonElement> entry : param.entrySet()) {
            String key = entry.getKey();

            if (!key.startsWith("a_") && !key.startsWith("b_")) continue;
            ControlButton controlButton = ControlButton.valueOf("LED_" + Integer.parseInt(key.split("_")[1]) + (key.startsWith("a_") ? "A" : "B"));
            boolean pressed = entry.getValue().getAsBoolean();

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
