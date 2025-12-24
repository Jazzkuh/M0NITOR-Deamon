package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
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
//            for (ControlButton button : Deamon.getModuleManager().get(AirliteModule.class).getHueButtons()) {
//                udpModule.writeStaticLed(button, ControlLedColor.RED);
//            }
//
//			udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.RED);
			room.turnOff();
		} else {
//            for (ControlButton button : Deamon.getModuleManager().get(AirliteModule.class).getHueButtons()) {
//                udpModule.writeStaticLed(button, ControlLedColor.GREEN);
//            }
//
//            udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.GREEN);
            room.turnOn();
		}
	}

	@Override
	public void startActions() {
//        udpModule.writeStaticLed(ControlButton.LED_8B, ControlLedColor.GREEN);
	}
}
