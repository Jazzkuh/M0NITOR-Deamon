package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import io.github.zeroone3010.yahueapi.v2.Group;
import lombok.SneakyThrows;

public class BrightLightsTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();

		for (ControlButton button : Deamon.getModuleManager().get(AuronModule.class).getHueButtons()) {
			// Reset all other buttons
            if (button != ControlButton.LED_7B) {
                EmberLedUtil.writeStaticLed(auronModule, button, ControlLedColor.YELLOW);
            }
		}

		if (auronModule.getEnabledButtons().contains("bright_lights")) {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_7B, ControlLedColor.GREEN);
            auronModule.getEnabledButtons().remove("bright_lights");

			Group room = hueController.getRoomByName("Studio");
			hueController.setScene(room, "Studio");
		} else {
            EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_7B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
            auronModule.getEnabledButtons().add("bright_lights");
			Group room = hueController.getRoomByName("Studio");
			hueController.setScene(room, "white");
		}
	}

	@Override
	public void startActions() {
		if (auronModule.getEnabledButtons().contains("bright_lights")) {
            EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_7B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		} else {
            EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_7B, ControlLedColor.GREEN);
		}
	}
}
