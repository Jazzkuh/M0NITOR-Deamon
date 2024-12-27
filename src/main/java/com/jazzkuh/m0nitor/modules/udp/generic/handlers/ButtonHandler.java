package com.jazzkuh.m0nitor.modules.udp.generic.handlers;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ButtonTrigger;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerType;
import com.jazzkuh.m0nitor.framework.udp.GenericHandler;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.airlite.registry.ButtonTriggerRegistry;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;

public class ButtonHandler extends GenericHandler {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);
    private final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);

    @Override
    public boolean shouldProcess(byte size, byte cmd) {
        return size == (byte) 0x04 && cmd == (byte) 0xC4;
    }

    @Override
    public void process(byte size, byte cmd, byte[] data) {
        int buttonId = data[10];
        int pressedValueA = data[4];
        int pressedValueB = data[5];

        ControlButton controlButton = ControlButton.getButton(buttonId);
        if (controlButton == null) return;

        boolean pressed = controlButton.getRow().equals("A") && pressedValueA == controlButton.getPressedValue() || controlButton.getRow().equals("B") && pressedValueB == controlButton.getPressedValue();
        ButtonTrigger buttonTrigger = new ButtonTrigger(controlButton, pressed ? TriggerType.BUTTON_PRESSED : TriggerType.BUTTON_RELEASED);
        TriggerAction triggerAction = ButtonTriggerRegistry.getAction(buttonTrigger);

        if (controlButton.isHasPressedColor() && ButtonTriggerRegistry.isKnownButton(controlButton)) {
            if (pressed) udpModule.writeStaticLed(controlButton, ControlLedColor.RED);
            else udpModule.writeStaticLed(controlButton, ControlLedColor.GREEN);
        }

        if (triggerAction != null) {
            triggerAction.process();
            udpModule.getLogger().info("Triggered action for button " + controlButton + ": " + triggerAction.getClass().getSimpleName());
        }
    }
}
