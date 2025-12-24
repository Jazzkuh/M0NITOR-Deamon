package com.jazzkuh.m0nitor.modules.auron.trigger.fader.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import de.labystudio.spotifyapi.model.MediaKey;
import lombok.SneakyThrows;

public class CueOffTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();
		if (musicEngine.isPlaying()) {
			musicEngine.pressMediaKey(MediaKey.PLAY_PAUSE);

			if (musicEngine.getSpotifyAPI().getPosition() < 3000) return;
			musicEngine.pressMediaKey(MediaKey.PLAY_PAUSE);
		}
	}
}
