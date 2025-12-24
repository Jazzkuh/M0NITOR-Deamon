package com.jazzkuh.m0nitor.modules.auron.trigger.fader;

import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

public class RegularLightTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
        auronModule.setMicrophoneOn(-1);
		PhilipsWizLightController.setRGBColor(BulbRegistry.getBulbByName("studio_led_strip2"), 255, 0, 0, 100);
		PhilipsWizLightController.setState(BulbRegistry.getBulbByName("studio_led_strip2"), false);
	}
}
