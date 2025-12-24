package com.jazzkuh.m0nitor.modules.auron.registry;

import com.jazzkuh.m0nitor.framework.auron.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.OnAirLightTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.RegularLightTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.music.CueOffTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.music.CueOnTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.music.MusicPauseTrigger;
import com.jazzkuh.m0nitor.modules.auron.trigger.fader.music.MusicSkipStartTrigger;

import java.util.HashMap;
import java.util.Map;

public class ChannelTriggerRegistry {
	private static Map<ChannelTrigger, Class<? extends TriggerAction>> triggers = new HashMap<>();
    private static final int spotifyChannel = 10;

	static {
		registerAction(new ChannelTrigger(TriggerType.MICROPHONE_ON), OnAirLightTrigger.class);
		registerAction(new ChannelTrigger(TriggerType.MICROPHONE_OFF), RegularLightTrigger.class);
		registerAction(new ChannelTrigger(spotifyChannel, TriggerType.MODULE_ACTIVE), MusicSkipStartTrigger.class);
		registerAction(new ChannelTrigger(spotifyChannel, TriggerType.MODULE_INACTIVE), MusicPauseTrigger.class);
		registerAction(new ChannelTrigger(spotifyChannel, TriggerType.FADER_OFF_CUE_ON), CueOnTrigger.class);
		registerAction(new ChannelTrigger(spotifyChannel, TriggerType.FADER_OFF_CUE_OFF), CueOffTrigger.class);
	}

	public static void registerAction(ChannelTrigger channelTrigger, Class<? extends TriggerAction> triggerClass) {
		triggers.put(channelTrigger, triggerClass);
	}

	public static TriggerAction getAction(ChannelTrigger channelTrigger) {
		Class<? extends TriggerAction> triggerClass = triggers.keySet().stream().filter(channelTrigger1 -> equals(channelTrigger1, channelTrigger)).map(triggers::get).findFirst().orElse(null);
		if (triggerClass == null) return null;

		try {
			return triggerClass.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean equals(ChannelTrigger channelTrigger, ChannelTrigger channelTrigger1) {
		return channelTrigger.getChannelId() == channelTrigger1.getChannelId() && channelTrigger.getTriggerType() == channelTrigger1.getTriggerType();
	}
}
