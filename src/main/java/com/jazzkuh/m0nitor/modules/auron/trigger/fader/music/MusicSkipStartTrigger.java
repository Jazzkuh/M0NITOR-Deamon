package com.jazzkuh.m0nitor.modules.auron.trigger.fader.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import de.labystudio.spotifyapi.model.MediaKey;
import lombok.SneakyThrows;

public class MusicSkipStartTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (auronModule.getEnabledButtons().contains("disable_spotify_fader")) return;

		MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();
		if (musicEngine.isPlaying()) {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_1A, ControlLedColor.RED);
			musicEngine.pressMediaKey(MediaKey.PLAY_PAUSE);
		}

		EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_1A, ControlLedColor.GREEN);
		if (!auronModule.getEnabledButtons().contains("fader_skip")) {
			musicEngine.pressMediaKey(MediaKey.NEXT);
		} else if (!musicEngine.isPlaying()) {
			musicEngine.pressMediaKey(MediaKey.PLAY_PAUSE);
		}

		if (!musicEngine.isPlaying() && musicEngine.getProvider() == MusicEngine.MusicEngineProvider.APPLE_MUSIC) {
			musicEngine.pressMediaKey(MediaKey.PLAY_PAUSE);
		}
	}
}
