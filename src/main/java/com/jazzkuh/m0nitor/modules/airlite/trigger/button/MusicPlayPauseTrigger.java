package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import lombok.SneakyThrows;

public class MusicPlayPauseTrigger extends TriggerAction {
	private final MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();

	@Override
	@SneakyThrows
	public void process() {
		if (musicEngine.isPlaying()) {
			musicEngine.playPause();
		} else {
			musicEngine.playPause();
		}
	}

	@Override
	public void startActions() {
		if (musicEngine.isPlaying()) {
			udpModule.writeStaticLed(ControlButton.LED_1A, ControlLedColor.GREEN);
		} else {
			udpModule.writeStaticLed(ControlButton.LED_1A, ControlLedColor.RED);
		}
	}
}
