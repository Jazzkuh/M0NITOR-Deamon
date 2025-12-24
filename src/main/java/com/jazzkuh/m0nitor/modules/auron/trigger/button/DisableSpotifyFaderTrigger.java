package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import lombok.SneakyThrows;

public class DisableSpotifyFaderTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (!auronModule.getEnabledButtons().contains("disable_spotify_fader")) {
//			udpModule.writeBlinkingLed(ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
            auronModule.getEnabledButtons().add("disable_spotify_fader");
		} else {
//			udpModule.writeStaticLed(ControlButton.LED_7A, ControlLedColor.GREEN);
            auronModule.getEnabledButtons().remove("disable_spotify_fader");
		}
	}

	@Override
	public void startActions() {
//		if (!airliteModule.getEnabledButtons().contains("disable_spotify_fader")) {
//			udpModule.writeStaticLed(ControlButton.LED_7A, ControlLedColor.GREEN);
//		} else {
//			udpModule.writeBlinkingLed(ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
//		}
	}
}
