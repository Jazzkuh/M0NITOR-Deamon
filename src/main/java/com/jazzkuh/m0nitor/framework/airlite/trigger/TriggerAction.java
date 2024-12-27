package com.jazzkuh.m0nitor.framework.airlite.trigger;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;

public abstract class TriggerAction implements TriggerActionImpl {
    protected final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);
    protected final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);

    public abstract void process();

    public void startActions() {
    }
}
