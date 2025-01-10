package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import io.github.zeroone3010.yahueapi.Room;
import lombok.SneakyThrows;

public class HueSceneTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();
		Room room = hueController.getRoomByName("Studio");
		hueController.setScene(room, "Studio");
	}
}
