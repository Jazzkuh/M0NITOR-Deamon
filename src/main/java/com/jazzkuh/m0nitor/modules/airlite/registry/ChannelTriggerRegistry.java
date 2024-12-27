package com.jazzkuh.m0nitor.modules.airlite.registry;

import com.jazzkuh.m0nitor.framework.airlite.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerType;
import com.jazzkuh.m0nitor.modules.airlite.trigger.fader.OnAirLightTrigger;
import com.jazzkuh.m0nitor.modules.airlite.trigger.fader.RegularLightTrigger;
import com.jazzkuh.m0nitor.modules.airlite.trigger.fader.music.CueOffTrigger;
import com.jazzkuh.m0nitor.modules.airlite.trigger.fader.music.CueOnTrigger;
import com.jazzkuh.m0nitor.modules.airlite.trigger.fader.music.MusicPauseTrigger;
import com.jazzkuh.m0nitor.modules.airlite.trigger.fader.music.MusicSkipStartTrigger;

import java.util.HashMap;
import java.util.Map;

public class ChannelTriggerRegistry {
	private static Map<ChannelTrigger, Class<? extends TriggerAction>> triggers = new HashMap<>();

	static {
		registerAction(new ChannelTrigger(TriggerType.MICROPHONE_ON), OnAirLightTrigger.class);
		registerAction(new ChannelTrigger(TriggerType.MICROPHONE_OFF), RegularLightTrigger.class);
		registerAction(new ChannelTrigger(8, TriggerType.FADER_AND_CHANNEL_ON), MusicSkipStartTrigger.class);
		registerAction(new ChannelTrigger(8, TriggerType.CHANNEL_OFF), MusicPauseTrigger.class);
		registerAction(new ChannelTrigger(8, TriggerType.FADER_OFF), MusicPauseTrigger.class);
		registerAction(new ChannelTrigger(8, TriggerType.FADER_OFF_CUE_ON), CueOnTrigger.class);
		registerAction(new ChannelTrigger(8, TriggerType.FADER_OFF_CUE_OFF), CueOffTrigger.class);
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
