package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import io.github.zeroone3010.yahueapi.v2.Group;
import lombok.SneakyThrows;

public class HueLightsTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();

        Group room = hueController.getRoomByName("Studio");
		if (room.isAnyOn()) {
			EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_8B, ControlLedColor.RED);
			room.turnOff();
		} else {
            EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_8B, ControlLedColor.GREEN);
            room.turnOn();
		}
	}

	@Override
	public void startActions() {
        EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_8B, ControlLedColor.GREEN);
	}
}
