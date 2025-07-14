package com.jazzkuh.m0nitor.modules.airlite;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.Fader;
import com.jazzkuh.m0nitor.framework.airlite.button.ButtonTrigger;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.airlite.registry.ButtonTriggerRegistry;
import com.jazzkuh.m0nitor.modules.airlite.tasks.FlashingTask;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import com.jazzkuh.m0nitor.utils.Concurrency;
import com.jazzkuh.modulemanager.generic.GenericModule;
import com.jazzkuh.modulemanager.generic.GenericModuleManager;
import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.model.Track;
import de.labystudio.spotifyapi.open.model.track.OpenTrack;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
public class AirliteModule extends GenericModule {
    private Map<Integer, Fader> faders = new HashMap<>();

    private boolean cueAux = false;
    private boolean autoCueCrm = false;
    private boolean autoCueAnnouncer = false;

    private long microphoneOn = -1;

    private final List<String> enabledButtons = new ArrayList<>();

    private int flashingSceneIndex = 0;

    private final ControlButton[] hueButtons = new ControlButton[]{
            ControlButton.LED_1B, ControlButton.LED_2B, ControlButton.LED_3B,
            ControlButton.LED_4B, ControlButton.LED_5B, ControlButton.LED_6B
    };


    private UDPModule udpModule;

    public AirliteModule(GenericModuleManager owningManager) {
        super(owningManager);
    }

    @Override
    public void onEnable() {
        this.udpModule = getOwningManager().get(UDPModule.class);
        Map<Integer, Byte> faderModules = Map.of(1, (byte) 0x00, 2, (byte) 0x01,
                3, (byte) 0x02, 4, (byte) 0x03, 5, (byte) 0x04,
                6, (byte) 0x05, 7, (byte) 0x06, 8, (byte) 0x07
        );

        for (int i = 1; i <= 8; i++) {
            faders.put(i, new Fader(i, (byte) 0, (byte) 1, faderModules.get(i)));
        }

        udpModule.writeStaticLed(ControlButton.ALL_LEDS, ControlLedColor.OFF);

        for (ButtonTrigger buttonTrigger : ButtonTriggerRegistry.getTriggers().keySet()) {
            ControlButton controlButton = buttonTrigger.getControlButton();
            udpModule.writeStaticLed(controlButton, ControlLedColor.GREEN);

            TriggerAction triggerAction = ButtonTriggerRegistry.getAction(buttonTrigger);
            if (triggerAction == null) continue;
            triggerAction.startActions();
        }

        registerComponent(new FlashingTask(this));
    }

    @Override
    public void onDisable() {
        udpModule.writeStaticLed(ControlButton.ALL_LEDS, ControlLedColor.OFF);
    }

    public JsonObject getSpotifyJson() {
        JsonObject spotify = new JsonObject();
        SpotifyAPI spotifyAPI = Deamon.getInstance().getMusicEngine().getSpotifyAPI();
        Track currentTrack = spotifyAPI.getTrack();

        if (currentTrack != null) {
            spotify.addProperty("track", currentTrack.getName());
            spotify.addProperty("artist", currentTrack.getArtist());
            spotify.addProperty("track_id", currentTrack.getId());
            spotify.addProperty("length", currentTrack.getLength());
        }

        if (spotifyAPI.hasPosition()) {
            spotify.addProperty("position", spotifyAPI.getPosition());
        }

        spotify.addProperty("playing", Deamon.getInstance().getMusicEngine().isPlaying());
        return spotify;
    }

    public void incrementFlashingSceneIndex() {
        flashingSceneIndex++;
    }
}
