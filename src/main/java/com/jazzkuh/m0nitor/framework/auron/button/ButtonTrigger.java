package com.jazzkuh.m0nitor.framework.auron.button;

import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ButtonTrigger {
    public final ControlButton controlButton;
    public TriggerType triggerType;
}
