package com.jazzkuh.m0nitor.modules.airlite.trigger.fader.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import lombok.SneakyThrows;

public class CueOffTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();
		if (musicEngine.isPlaying()) {
			musicEngine.playPause();

			if (musicEngine.getSpotifyAPI().getPosition() < 3000) return;
			musicEngine.previous();
		}
	}
}
