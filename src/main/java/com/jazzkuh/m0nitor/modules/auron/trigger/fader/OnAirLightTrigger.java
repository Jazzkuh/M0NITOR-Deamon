package com.jazzkuh.m0nitor.modules.auron.trigger.fader;

import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.Bulb;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class OnAirLightTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		long millis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        auronModule.setMicrophoneOn(millis);

        Bulb ledBulb = BulbRegistry.getBulbByName("studio_led");
        if (ledBulb == null) return;

		PhilipsWizLightController.setState(ledBulb, true);
	}
}
