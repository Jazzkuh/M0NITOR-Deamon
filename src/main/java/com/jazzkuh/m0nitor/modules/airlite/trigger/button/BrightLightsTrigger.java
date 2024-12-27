package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.Bulb;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

public class BrightLightsTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (airliteModule.getEnabledButtons().contains("bright_lights")) {
			udpModule.writeStaticLed(ControlButton.LED_8A, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("bright_lights");

			PhilipsWizLightController.setState(BulbRegistry.getBulbByName("studio_led_strip2"), false);
			for (Bulb bulb : BulbRegistry.getBulbsByGroups("scarlet")) {
				PhilipsWizLightController.setScene(bulb, PhilipsWizLightController.Scene.Sunset, 100);
			}

			for (Bulb bulb : BulbRegistry.getBulbsByGroups("studio")) {
				PhilipsWizLightController.setScene(bulb, PhilipsWizLightController.Scene.Sunset, 100);
			}

			for (Bulb bulb : BulbRegistry.getBulbsByGroups("warm_white")) {
				PhilipsWizLightController.setColorTemperature(bulb, 2200, 100);
			}
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_8A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("bright_lights");
			for (Bulb bulb : BulbRegistry.getAllBulbs()) {
				PhilipsWizLightController.setColorTemperature(bulb, 6500, 100);
			}
		}
	}

	@Override
	public void startActions() {
		if (airliteModule.getEnabledButtons().contains("bright_lights")) {
			udpModule.writeBlinkingLed(ControlButton.LED_8A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		} else {
			udpModule.writeStaticLed(ControlButton.LED_8A, ControlLedColor.GREEN);
		}
	}
}
