package com.jazzkuh.m0nitor.modules.auron.registry;

import com.jazzkuh.m0nitor.framework.auron.button.ButtonTrigger;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.OnAirLightTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.OnTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.RegularLightTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.music.MusicPauseTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.music.MusicSkipStartTrigger;

import java.util.HashMap;
import java.util.Map;

public class ChannelTriggerRegistry {
	private static Map<ChannelTrigger, TriggerAction> triggers = new HashMap<>();
    private static final int spotifyChannel = 10;

	static {
		registerAction(new ChannelTrigger(TriggerType.MICROPHONE_ON), new OnAirLightTrigger());
		registerAction(new ChannelTrigger(TriggerType.MICROPHONE_OFF), new RegularLightTrigger());
		registerAction(new ChannelTrigger(spotifyChannel, TriggerType.MODULE_ACTIVE), new MusicSkipStartTrigger());
		registerAction(new ChannelTrigger(spotifyChannel, TriggerType.MODULE_INACTIVE), new MusicPauseTrigger());
	}

    public static void registerAction(ChannelTrigger channelTrigger, TriggerAction triggerAction) {
        triggers.put(channelTrigger, triggerAction);
    }

    public static TriggerAction getAction(ChannelTrigger channelTrigger) {
        return triggers.keySet().stream().filter(channelTrigger1 -> equals(channelTrigger1, channelTrigger)).map(triggers::get).findFirst().orElse(null);
    }
	private static boolean equals(ChannelTrigger channelTrigger, ChannelTrigger channelTrigger1) {
		return channelTrigger.getChannelId() == channelTrigger1.getChannelId() && channelTrigger.getTriggerType() == channelTrigger1.getTriggerType();
	}
}
