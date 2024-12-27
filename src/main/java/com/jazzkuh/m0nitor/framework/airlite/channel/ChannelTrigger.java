package com.jazzkuh.m0nitor.framework.airlite.channel;

import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelTrigger {
    public final int channelId;
    public TriggerType triggerType;

    public ChannelTrigger(TriggerType triggerType) {
        this.channelId = -1;
        this.triggerType = triggerType;
    }
}
