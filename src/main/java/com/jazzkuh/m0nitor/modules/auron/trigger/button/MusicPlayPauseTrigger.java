package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import de.labystudio.spotifyapi.model.MediaKey;
import lombok.SneakyThrows;

public class MusicPlayPauseTrigger extends TriggerAction {
	private final MusicEngine musicEngine = Deamon.getInstance().getMusicEngine();

	@Override
	@SneakyThrows
	public void process() {
		musicEngine.pressMediaKey(MediaKey.PLAY_PAUSE);
	}

	@Override
	public void startActions() {
		if (musicEngine.isPlaying()) {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_1A, ControlLedColor.GREEN);
		} else {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_1A, ControlLedColor.RED);
		}
	}
}
