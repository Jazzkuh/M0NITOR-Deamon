package com.jazzkuh.m0nitor.modules.omniplayer.trigger;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.omniplayer.OmniModule;
import com.jazzkuh.m0nitor.modules.omniplayer.gpio.GpiUtils;
import lombok.SneakyThrows;

public class GpiThreeTrigger extends TriggerAction {
    private final OmniModule omniModule = Deamon.getModuleManager().get(OmniModule.class);

	@Override
	@SneakyThrows
	public void process() {
        GpiUtils.pulseContact(omniModule.getGpioModule(), 3);
	}
}
