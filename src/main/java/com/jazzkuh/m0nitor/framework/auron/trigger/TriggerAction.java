package com.jazzkuh.m0nitor.framework.auron.trigger;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;

public abstract class TriggerAction implements TriggerActionImpl {
    protected final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    public abstract void process();

    public void startActions() {
    }
}
