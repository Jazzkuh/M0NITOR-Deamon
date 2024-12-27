package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.Bulb;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

public class SceneTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (airliteModule.getEnabledButtons().contains("scene")) {
			udpModule.writeStaticLed(ControlButton.LED_7A, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("scene");

			PhilipsWizLightController.setState(BulbRegistry.getBulbByName("studio_led_strip2"), false);
			for (Bulb bulb : BulbRegistry.getBulbsByGroups("studio")) {
				PhilipsWizLightController.setScene(bulb, PhilipsWizLightController.Scene.Sunset, 100);
			}
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("scene");

			for (Bulb bulb : BulbRegistry.getBulbsByGroup("studio")) {
				PhilipsWizLightController.setScene(bulb, PhilipsWizLightController.Scene.Romance, 100);
			}
		}
	}

	@Override
	public void startActions() {
		if (airliteModule.getEnabledButtons().contains("scene")) {
			udpModule.writeBlinkingLed(ControlButton.LED_7A, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		} else {
			udpModule.writeStaticLed(ControlButton.LED_7A, ControlLedColor.GREEN);
		}
	}
}
