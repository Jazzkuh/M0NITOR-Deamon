package com.jazzkuh.m0nitor.modules.auron.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
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

		for (ControlButton button : Deamon.getModuleManager().get(AuronModule.class).getHueButtons()) {
			if (button == activeButton) {
				EmberLedUtil.writeStaticLed(auronModule, button, ControlLedColor.GREEN);
				continue;
			}

			EmberLedUtil.writeStaticLed(auronModule, button, ControlLedColor.YELLOW);
		}

		hueController.setLastScene(getScene());
		hueController.setScene(room, getScene());
	}
}
