package com.jazzkuh.m0nitor.modules.airlite.registry;

import com.jazzkuh.m0nitor.framework.airlite.button.ButtonTrigger;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerType;
import com.jazzkuh.m0nitor.modules.airlite.trigger.button.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ButtonTriggerRegistry {
	@Getter
	private static Map<ButtonTrigger, TriggerAction> triggers = new HashMap<>();

	static {
		registerAction(new ButtonTrigger(ControlButton.LED_1A, TriggerType.BUTTON_PRESSED), new MusicPlayPauseTrigger());
		registerAction(new ButtonTrigger(ControlButton.LED_2A, TriggerType.BUTTON_PRESSED), new MusicSkipTrigger());
		registerAction(new ButtonTrigger(ControlButton.LED_3A, TriggerType.BUTTON_PRESSED), new MusicPreviousTrigger());

		registerAction(new ButtonTrigger(ControlButton.LED_7A, TriggerType.BUTTON_PRESSED), new DisableSpotifyFaderTrigger());
		registerAction(new ButtonTrigger(ControlButton.LED_8A, TriggerType.BUTTON_PRESSED), new FaderSkipToggleTrigger());

		registerAction(new ButtonTrigger(ControlButton.LED_6B, TriggerType.BUTTON_PRESSED), new FlashingToggleTrigger());
		registerAction(new ButtonTrigger(ControlButton.LED_7B, TriggerType.BUTTON_PRESSED), new BrightLightsTrigger());
        registerAction(new ButtonTrigger(ControlButton.LED_8B, TriggerType.BUTTON_PRESSED), new HueLightsTrigger());

		registerAction(new ButtonTrigger(ControlButton.LED_1B, TriggerType.BUTTON_PRESSED), new HueSceneTrigger("Studio", ControlButton.LED_1B));
		registerAction(new ButtonTrigger(ControlButton.LED_2B, TriggerType.BUTTON_PRESSED), new HueSceneTrigger("Chinatown", ControlButton.LED_2B));
		registerAction(new ButtonTrigger(ControlButton.LED_3B, TriggerType.BUTTON_PRESSED), new HueSceneTrigger("Amberbloesem", ControlButton.LED_3B));
		registerAction(new ButtonTrigger(ControlButton.LED_4B, TriggerType.BUTTON_PRESSED), new HueSceneTrigger("Fairfax", ControlButton.LED_4B));
		registerAction(new ButtonTrigger(ControlButton.LED_5B, TriggerType.BUTTON_PRESSED), new HueSceneTrigger("Miami", ControlButton.LED_5B));
	}

	public static void registerAction(ButtonTrigger buttonTrigger, TriggerAction triggerAction) {
		triggers.put(buttonTrigger, triggerAction);
	}

	public static TriggerAction getAction(ButtonTrigger buttonTrigger) {
		return triggers.keySet().stream().filter(buttonTrigger1 -> equals(buttonTrigger1, buttonTrigger)).map(triggers::get).findFirst().orElse(null);
	}

	public static boolean isKnownButton(ControlButton controlButton) {
		return triggers.keySet().stream().anyMatch(buttonTrigger -> buttonTrigger.getControlButton() == controlButton);
	}

	private static boolean equals(ButtonTrigger buttonTrigger, ButtonTrigger buttonTrigger1) {
		return buttonTrigger1.getControlButton() == buttonTrigger.getControlButton() && buttonTrigger1.getTriggerType() == buttonTrigger.getTriggerType();
	}
}
