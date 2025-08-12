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

public class FlashingToggleTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();
		Group room = hueController.getRoomByName("Studio");

		for (ControlButton button : Deamon.getModuleManager().get(AirliteModule.class).getHueButtons()) {
			udpModule.writeStaticLed(button, ControlLedColor.GREEN);
		}

		if (!airliteModule.getEnabledButtons().contains("flashing")) {
			udpModule.writeBlinkingLed(ControlButton.LED_7B, ControlLedColor.GREEN, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("flashing");
		} else {
			udpModule.writeStaticLed(ControlButton.LED_7B, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("flashing");
			hueController.setScene(room, hueController.getLastScene());
		}
	}

	@Override
	public void startActions() {
		if (!airliteModule.getEnabledButtons().contains("flashing")) {
			udpModule.writeStaticLed(ControlButton.LED_7B, ControlLedColor.GREEN);
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_7B, ControlLedColor.GREEN, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		}
	}
}
