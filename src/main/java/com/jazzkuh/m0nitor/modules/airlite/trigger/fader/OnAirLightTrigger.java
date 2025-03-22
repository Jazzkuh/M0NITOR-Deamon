package com.jazzkuh.m0nitor.modules.airlite.trigger.fader;

import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class OnAirLightTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		long millis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

		airliteModule.setMicrophoneOn(millis);
		PhilipsWizLightController.setState(BulbRegistry.getBulbByName("studio_led_strip2"), true);
	}
}
