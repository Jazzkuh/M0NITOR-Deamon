package com.jazzkuh.m0nitor.modules.auron.trigger.fader.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import de.labystudio.spotifyapi.model.MediaKey;
import lombok.SneakyThrows;

public class MusicPauseTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (auronModule.getEnabledButtons().contains("disable_spotify_fader")) return;

		MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();
		if (musicEngine.isPlaying()) {
//			udpModule.writeStaticLed(ControlButton.LED_1A, ControlLedColor.RED);
			musicEngine.pressMediaKey(MediaKey.PLAY_PAUSE);
		}
	}
}
