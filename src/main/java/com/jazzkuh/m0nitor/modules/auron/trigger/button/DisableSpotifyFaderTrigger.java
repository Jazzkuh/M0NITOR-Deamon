package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import lombok.SneakyThrows;

public class DisableSpotifyFaderTrigger extends TriggerAction {
    @Override
	@SneakyThrows
	public void process() {
		if (!auronModule.getEnabledButtons().contains("disable_spotify_fader")) {
			EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
            auronModule.getEnabledButtons().add("disable_spotify_fader");
		} else {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_7A, ControlLedColor.GREEN);
            auronModule.getEnabledButtons().remove("disable_spotify_fader");
		}
	}

	@Override
	public void startActions() {
		if (!auronModule.getEnabledButtons().contains("disable_spotify_fader")) {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_7A, ControlLedColor.GREEN);
		} else {
			EmberLedUtil.writeBlinkingLed(auronModule, ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		}
	}
}
