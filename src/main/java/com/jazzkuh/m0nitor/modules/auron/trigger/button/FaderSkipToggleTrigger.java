package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import lombok.SneakyThrows;

public class FaderSkipToggleTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (!auronModule.getEnabledButtons().contains("fader_skip")) {
			EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_8A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			auronModule.getEnabledButtons().add("fader_skip");
		} else {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_8A, ControlLedColor.GREEN);
			auronModule.getEnabledButtons().remove("fader_skip");
		}
	}

	@Override
	public void startActions() {
		if (!auronModule.getEnabledButtons().contains("fader_skip")) {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_8A, ControlLedColor.GREEN);
		} else {
			EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_8A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		}
	}
}
