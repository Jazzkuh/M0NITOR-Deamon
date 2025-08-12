package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import io.github.zeroone3010.yahueapi.v2.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public class HueSceneTrigger extends TriggerAction {
	private final String scene;
	private final ControlButton activeButton;
	@Override
	@SneakyThrows
	public void process() {
		HueController hueController = Deamon.getInstance().getHueController();
		Group room = hueController.getRoomByName("Studio");
		if (room == null) return;

		for (ControlButton button : Deamon.getModuleManager().get(AirliteModule.class).getHueButtons()) {
			if (button == activeButton) {
				udpModule.writeBlinkingLed(button, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
				continue;
			}

			udpModule.writeStaticLed(button, ControlLedColor.GREEN);
		}

		hueController.setLastScene(getScene());
		hueController.setScene(room, getScene());
	}
}
