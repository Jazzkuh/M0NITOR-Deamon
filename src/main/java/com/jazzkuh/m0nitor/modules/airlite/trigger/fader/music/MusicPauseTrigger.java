package com.jazzkuh.m0nitor.modules.airlite.trigger.fader.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import lombok.SneakyThrows;

public class MusicPauseTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (airliteModule.getEnabledButtons().contains("disable_spotify_fader")) return;

		MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();
		if (musicEngine.isPlaying()) {
			udpModule.writeStaticLed(ControlButton.LED_1A, ControlLedColor.RED);
			musicEngine.playPause();
		}
	}
}
