package com.jazzkuh.m0nitor.modules.omniplayer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.framework.auron.button.ButtonTrigger;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.channel.ChannelTrigger;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerType;
import com.jazzkuh.m0nitor.modules.auron.listeners.AuronSocket;
import com.jazzkuh.m0nitor.modules.auron.registry.ButtonTriggerRegistry;
import com.jazzkuh.m0nitor.modules.auron.registry.ChannelTriggerRegistry;
import com.jazzkuh.m0nitor.modules.auron.tasks.FlashingTask;
import com.jazzkuh.m0nitor.modules.omniplayer.gpio.GpioModule;
import com.jazzkuh.m0nitor.modules.omniplayer.trigger.*;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.Bulb;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import com.jazzkuh.modulemanager.generic.GenericModule;
import com.jazzkuh.modulemanager.generic.GenericModuleManager;
import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.model.Track;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class OmniModule extends GenericModule {
    @Getter
    private GpioModule gpioModule;

    public OmniModule(GenericModuleManager owningManager) {
        super(owningManager);
    }

    @Override
    public void onLoad() {
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(5, TriggerType.MODULE_ACTIVE), GpiOneTrigger.class);
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(5, TriggerType.MODULE_INACTIVE), GpiTwoTrigger.class);

        ChannelTriggerRegistry.registerAction(new ChannelTrigger(6, TriggerType.MODULE_ACTIVE), GpiFiveTrigger.class);
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(6, TriggerType.MODULE_INACTIVE), GpiSixTrigger.class);

        ChannelTriggerRegistry.registerAction(new ChannelTrigger(7, TriggerType.MODULE_ACTIVE), GpiSevenTrigger.class);
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(7, TriggerType.MODULE_INACTIVE), GpiEightTrigger.class);
    }

    @Override
    public void onEnable() {
        gpioModule = new GpioModule("192.168.1.183", 4740);
        gpioModule.setResponseHandler( response -> {
            System.out.println("Received GPIO response: " + response);
        });

        gpioModule.connect();
    }

    @Override
    public void onDisable() {
    }

}
