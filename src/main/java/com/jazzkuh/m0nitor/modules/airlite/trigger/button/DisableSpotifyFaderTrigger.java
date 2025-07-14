package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import lombok.SneakyThrows;

public class DisableSpotifyFaderTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (!airliteModule.getEnabledButtons().contains("disable_spotify_fader")) {
			udpModule.writeBlinkingLed(ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("disable_spotify_fader");
		} else {
			udpModule.writeStaticLed(ControlButton.LED_7A, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("disable_spotify_fader");
		}
	}

	@Override
	public void startActions() {
		if (!airliteModule.getEnabledButtons().contains("disable_spotify_fader")) {
			udpModule.writeStaticLed(ControlButton.LED_7A, ControlLedColor.GREEN);
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		}
	}
}
