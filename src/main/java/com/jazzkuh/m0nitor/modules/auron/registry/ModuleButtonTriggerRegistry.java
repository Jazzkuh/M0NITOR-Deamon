package com.jazzkuh.m0nitor.modules.auron.registry;

import com.jazzkuh.m0nitor.framework.auron.button.ModuleButton;
import com.jazzkuh.m0nitor.framework.auron.button.ModuleButtonTrigger;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.modules.auron.trigger.module.SubBusTrigger;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ModuleButtonTriggerRegistry {
	@Getter
	private static Map<ModuleButtonTrigger, TriggerAction> triggers = new HashMap<>();

	static {
        registerAction(new ModuleButtonTrigger(ModuleButton.MOD_4_TOP_LEFT, TriggerType.MODULE_BUTTON_PRESSED), new SubBusTrigger(4, ModuleButton.MOD_4_TOP_LEFT));
		registerAction(new ModuleButtonTrigger(ModuleButton.MOD_5_TOP_LEFT, TriggerType.MODULE_BUTTON_PRESSED), new SubBusTrigger(5, ModuleButton.MOD_5_TOP_LEFT));
        registerAction(new ModuleButtonTrigger(ModuleButton.MOD_6_TOP_LEFT, TriggerType.MODULE_BUTTON_PRESSED), new SubBusTrigger(6, ModuleButton.MOD_6_TOP_LEFT));
        registerAction(new ModuleButtonTrigger(ModuleButton.MOD_7_TOP_LEFT, TriggerType.MODULE_BUTTON_PRESSED), new SubBusTrigger(7, ModuleButton.MOD_7_TOP_LEFT));
        registerAction(new ModuleButtonTrigger(ModuleButton.MOD_8_TOP_LEFT, TriggerType.MODULE_BUTTON_PRESSED), new SubBusTrigger(8, ModuleButton.MOD_8_TOP_LEFT));
        registerAction(new ModuleButtonTrigger(ModuleButton.MOD_9_TOP_LEFT, TriggerType.MODULE_BUTTON_PRESSED), new SubBusTrigger(9, ModuleButton.MOD_9_TOP_LEFT));
        registerAction(new ModuleButtonTrigger(ModuleButton.MOD_10_TOP_LEFT, TriggerType.MODULE_BUTTON_PRESSED), new SubBusTrigger(10, ModuleButton.MOD_10_TOP_LEFT));
	}

	public static void registerAction(ModuleButtonTrigger buttonTrigger, TriggerAction triggerAction) {
		triggers.put(buttonTrigger, triggerAction);
	}

	public static TriggerAction getAction(ModuleButtonTrigger buttonTrigger) {
		return triggers.keySet().stream().filter(buttonTrigger1 -> equals(buttonTrigger1, buttonTrigger)).map(triggers::get).findFirst().orElse(null);
	}

	public static boolean isKnownButton(ModuleButton moduleButton) {
		return triggers.keySet().stream().anyMatch(buttonTrigger -> buttonTrigger.getModuleButton() == moduleButton);
	}

	private static boolean equals(ModuleButtonTrigger buttonTrigger, ModuleButtonTrigger buttonTrigger1) {
		return buttonTrigger1.getModuleButton() == buttonTrigger.getModuleButton() && buttonTrigger1.getTriggerType() == buttonTrigger.getTriggerType();
	}
}
