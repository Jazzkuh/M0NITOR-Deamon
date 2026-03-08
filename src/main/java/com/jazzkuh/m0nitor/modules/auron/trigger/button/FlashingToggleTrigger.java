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

public class FlashingToggleTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();
		Group room = hueController.getRoomByName("Studio");

		for (ControlButton button : Deamon.getModuleManager().get(AuronModule.class).getHueButtons()) {
			EmberLedUtil.writeStaticLed(auronModule, button, ControlLedColor.YELLOW);
		}

		if (!auronModule.getEnabledButtons().contains("flashing")) {
			EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_6B, ControlLedColor.YELLOW, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
            auronModule.getEnabledButtons().add("flashing");
		} else {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_6B, ControlLedColor.YELLOW);
            auronModule.getEnabledButtons().remove("flashing");
			hueController.setScene(room, hueController.getLastScene());
		}
	}

	@Override
	public void startActions() {
		if (!auronModule.getEnabledButtons().contains("flashing")) {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_6B, ControlLedColor.YELLOW);
		} else {
			EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_6B, ControlLedColor.YELLOW, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		}
	}
}
