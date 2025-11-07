package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import io.github.zeroone3010.yahueapi.v2.Group;
import lombok.SneakyThrows;

public class BrightLightsTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();

		for (ControlButton button : Deamon.getModuleManager().get(AirliteModule.class).getHueButtons()) {
			udpModule.writeStaticLed(button, ControlLedColor.GREEN);
		}

		if (airliteModule.getEnabledButtons().contains("bright_lights")) {
			udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("bright_lights");

			Group room = hueController.getRoomByName("Studio");
			hueController.setScene(room, "Studio");
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_8B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("bright_lights");
			Group room = hueController.getRoomByName("Studio");
			hueController.setScene(room, "white");
		}
	}

	@Override
	public void startActions() {
		if (airliteModule.getEnabledButtons().contains("bright_lights")) {
			udpModule.writeBlinkingLed(ControlButton.LED_8B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		} else {
			udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.GREEN);
		}
	}
}
