package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import de.labystudio.spotifyapi.model.MediaKey;
import lombok.SneakyThrows;

public class MusicSkipTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();
		musicEngine.pressMediaKey(MediaKey.NEXT);
	}
}
