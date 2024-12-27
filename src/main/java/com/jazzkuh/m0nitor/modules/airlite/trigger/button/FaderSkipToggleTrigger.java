package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import lombok.SneakyThrows;

public class FaderSkipToggleTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (!airliteModule.getEnabledButtons().contains("fader_skip")) {
			udpModule.writeBlinkingLed(ControlButton.LED_8B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("fader_skip");
		} else {
			udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("fader_skip");
		}
	}

	@Override
	public void startActions() {
		if (!airliteModule.getEnabledButtons().contains("fader_skip")) {
			udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.GREEN);
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_8B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		}
	}
}
