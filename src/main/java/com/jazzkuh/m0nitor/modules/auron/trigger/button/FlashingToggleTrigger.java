package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
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
//			udpModule.writeStaticLed(button, ControlLedColor.GREEN);
		}

		if (!auronModule.getEnabledButtons().contains("flashing")) {
//			udpModule.writeBlinkingLed(ControlButton.LED_6B, ControlLedColor.GREEN, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
            auronModule.getEnabledButtons().add("flashing");
		} else {
//			udpModule.writeStaticLed(ControlButton.LED_6B, ControlLedColor.GREEN);
            auronModule.getEnabledButtons().remove("flashing");
			hueController.setScene(room, hueController.getLastScene());
		}
	}

	@Override
	public void startActions() {
//		if (!airliteModule.getEnabledButtons().contains("flashing")) {
//			udpModule.writeStaticLed(ControlButton.LED_6B, ControlLedColor.GREEN);
//		} else {
//			udpModule.writeBlinkingLed(ControlButton.LED_6B, ControlLedColor.GREEN, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
//		}
	}
}
