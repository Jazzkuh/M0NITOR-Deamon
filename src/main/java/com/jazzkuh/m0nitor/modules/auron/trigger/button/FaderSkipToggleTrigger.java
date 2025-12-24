package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import lombok.SneakyThrows;

public class FaderSkipToggleTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (!auronModule.getEnabledButtons().contains("fader_skip")) {
//			udpModule.writeBlinkingLed(ControlButton.LED_8A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			auronModule.getEnabledButtons().add("fader_skip");
		} else {
//			udpModule.writeStaticLed(ControlButton.LED_8A, ControlLedColor.GREEN);
			auronModule.getEnabledButtons().remove("fader_skip");
		}
	}

	@Override
	public void startActions() {
//		if (!airliteModule.getEnabledButtons().contains("fader_skip")) {
//			udpModule.writeStaticLed(ControlButton.LED_8A, ControlLedColor.GREEN);
//		} else {
//			udpModule.writeBlinkingLed(ControlButton.LED_8A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
//		}
	}
}
