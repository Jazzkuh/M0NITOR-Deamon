package com.jazzkuh.m0nitor.framework.airlite.button;

import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ButtonTrigger {
    public final ControlButton controlButton;
    public TriggerType triggerType;
}
