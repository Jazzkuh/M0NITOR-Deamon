package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.Bulb;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import io.github.zeroone3010.yahueapi.v2.Group;
import lombok.SneakyThrows;

public class BrightLightsTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();

		for (ControlButton button : Deamon.getModuleManager().get(AirliteModule.class).getHueButtons()) {
			udpModule.writeStaticLed(button, ControlLedColor.GREEN);
		}

		if (airliteModule.getEnabledButtons().contains("bright_lights")) {
			udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("bright_lights");

			PhilipsWizLightController.setState(BulbRegistry.getBulbByName("studio_led_strip2"), false);
			for (Bulb bulb : BulbRegistry.getBulbsByGroups("scarlet")) {
				PhilipsWizLightController.setScene(bulb, PhilipsWizLightController.Scene.Sunset, 100);
			}

			for (Bulb bulb : BulbRegistry.getBulbsByGroups("warm_white")) {
				PhilipsWizLightController.setColorTemperature(bulb, 2200, 100);
			}

			Group room = hueController.getRoomByName("Studio");
			hueController.setScene(room, "Studio");
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_8B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("bright_lights");
			Group room = hueController.getRoomByName("Studio");
			hueController.setScene(room, "white");

			for (Bulb bulb : BulbRegistry.getAllBulbs()) {
				PhilipsWizLightController.setColorTemperature(bulb, 6500, 100);
			}
		}
	}

	@Override
	public void startActions() {
		if (airliteModule.getEnabledButtons().contains("bright_lights")) {
			udpModule.writeBlinkingLed(ControlButton.LED_8B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		} else {
			udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.GREEN);
		}
	}
}
