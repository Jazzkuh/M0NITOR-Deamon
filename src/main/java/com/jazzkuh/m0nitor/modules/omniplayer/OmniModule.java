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
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(5, TriggerType.MODULE_ACTIVE), new GpiOneTrigger());
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(5, TriggerType.MODULE_INACTIVE), new GpiTwoTrigger());
//        ChannelTriggerRegistry.registerAction(new ChannelTrigger(5, TriggerType.CUE_ON), new GpiElevenTrigger());
//        ChannelTriggerRegistry.registerAction(new ChannelTrigger(5, TriggerType.FADER_OFF_CUE_ON), new GpiElevenTrigger());
//        ChannelTriggerRegistry.registerAction(new ChannelTrigger(5, TriggerType.FADER_OFF_CUE_OFF), new GpiTwelveTrigger());
//        ChannelTriggerRegistry.registerAction(new ChannelTrigger(5, TriggerType.CUE_OFF), new GpiTwelveTrigger());

        ChannelTriggerRegistry.registerAction(new ChannelTrigger(6, TriggerType.MODULE_ACTIVE), new GpiFiveTrigger());
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(6, TriggerType.MODULE_INACTIVE), new GpiSixTrigger());

        ChannelTriggerRegistry.registerAction(new ChannelTrigger(7, TriggerType.MODULE_ACTIVE), new GpiSevenTrigger());
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(7, TriggerType.MODULE_INACTIVE), new GpiEightTrigger());

        ChannelTriggerRegistry.registerAction(new ChannelTrigger(8, TriggerType.MODULE_ACTIVE), new GpiNineTrigger());
        ChannelTriggerRegistry.registerAction(new ChannelTrigger(8, TriggerType.MODULE_INACTIVE), new GpiTenTrigger());

//        ChannelTriggerRegistry.registerAction(new ChannelTrigger(10, TriggerType.CUE_ON), new GpiThriteenTrigger());
//        ChannelTriggerRegistry.registerAction(new ChannelTrigger(10, TriggerType.FADER_OFF_CUE_ON), new GpiThriteenTrigger());
//        ChannelTriggerRegistry.registerAction(new ChannelTrigger(10, TriggerType.FADER_OFF_CUE_OFF), new GpiFourteenTrigger());
//        ChannelTriggerRegistry.registerAction(new ChannelTrigger(10, TriggerType.CUE_OFF), new GpiFourteenTrigger());
    }

    @Override
    public void onEnable() {
        gpioModule = new GpioModule("192.168.1.183", 4740);
        gpioModule.setResponseHandler( response -> {
            Deamon.getLogger().info("Received GPIO response: " + response);
        });

        gpioModule.connect();
    }

    @Override
    public void onDisable() {
        gpioModule.disconnect();
    }
}
