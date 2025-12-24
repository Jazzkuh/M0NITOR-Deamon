package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
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
		}

		if (auronModule.getEnabledButtons().contains("bright_lights")) {
//			udpModule.writeStaticLed(ControlButton.LED_7B, ControlLedColor.GREEN);
            auronModule.getEnabledButtons().remove("bright_lights");

			Group room = hueController.getRoomByName("Studio");
			hueController.setScene(room, "Studio");
		} else {
//			udpModule.writeBlinkingLed(ControlButton.LED_7B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
            auronModule.getEnabledButtons().add("bright_lights");
			Group room = hueController.getRoomByName("Studio");
			hueController.setScene(room, "white");
		}
	}

	@Override
	public void startActions() {
//		if (airliteModule.getEnabledButtons().contains("bright_lights")) {
//			udpModule.writeBlinkingLed(ControlButton.LED_7B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
//		} else {
//			udpModule.writeStaticLed(ControlButton.LED_7B, ControlLedColor.GREEN);
//		}
	}
}
