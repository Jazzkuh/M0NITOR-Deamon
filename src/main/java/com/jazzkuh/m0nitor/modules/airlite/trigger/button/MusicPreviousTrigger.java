package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import lombok.SneakyThrows;

public class MusicPreviousTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();
		musicEngine.previous();
	}
}
