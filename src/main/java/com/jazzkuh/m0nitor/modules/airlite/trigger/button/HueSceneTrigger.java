package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import io.github.zeroone3010.yahueapi.v2.Group;
import lombok.SneakyThrows;

public class HueSceneTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();
		Group room = hueController.getRoomByName("Studio");
		if (airliteModule.getEnabledButtons().contains("scene")) {
			udpModule.writeStaticLed(ControlButton.LED_7A, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("scene");

			hueController.setScene(room, "Chinatown");
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("scene");

			hueController.setScene(room, "Disturbia");
		}
	}
}
