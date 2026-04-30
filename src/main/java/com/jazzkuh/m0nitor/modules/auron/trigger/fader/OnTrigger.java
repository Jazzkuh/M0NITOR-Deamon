package com.jazzkuh.m0nitor.modules.auron.trigger.fader;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import lombok.SneakyThrows;

public class OnTrigger extends TriggerAction {

    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);
    private final int channel;

    public OnTrigger(int channel) {
        this.channel = channel;
    }

	@Override
	@SneakyThrows
	public void process() {
        EmberLedUtil.writeChannelLed(auronModule, channel, ControlLedColor.GREEN);
	}
}
