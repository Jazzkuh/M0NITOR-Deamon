package com.jazzkuh.m0nitor.modules.auron;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.Module;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.modules.auron.listeners.AuronSocket;
import com.jazzkuh.m0nitor.modules.auron.tasks.FlashingTask;
import com.jazzkuh.m0nitor.modules.web.WebModule;
import com.jazzkuh.modulemanager.generic.GenericModule;
import com.jazzkuh.modulemanager.generic.GenericModuleManager;
import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.model.Track;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class AuronModule extends GenericModule {
    private Map<Integer, Module> modules = new HashMap<>();

    @Getter @Setter
    private Map<String, Double> meteringValues = new HashMap<>();

    private boolean cueAux = false;
    private boolean autoCueCrm = false;
    private boolean autoCueAnnouncer = false;
    private boolean cueAir = false;

    private long microphoneOn = -1;

    private final List<String> enabledButtons = new ArrayList<>();

    private int flashingSceneIndex = 0;

    private final ControlButton[] hueButtons = new ControlButton[]{
            ControlButton.LED_1B, ControlButton.LED_2B, ControlButton.LED_3B,
            ControlButton.LED_4B, ControlButton.LED_5B, ControlButton.LED_6B
    };

    @Getter
    private AuronSocket auronSocket;

    private WebModule webModule;

    public AuronModule(GenericModuleManager owningManager) {
        super(owningManager);
    }

    @Override
    public void onLoad() {
        for (int i = 1; i <= 10; i++) {
            modules.put(i, new Module(i, false, 0, false));
        }
    }

    @Override
    public void onEnable() {
        this.webModule = getOwningManager().get(WebModule.class);

        this.auronSocket = new AuronSocket(this, webModule);
        this.auronSocket.connect();

        registerComponent(new FlashingTask(this));

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (this.auronSocket != null && this.auronSocket.isOpen()) {
                this.auronSocket.send("{\"msg\":\"ping\"}");
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
    }

    public void sendToSocket(String command, JsonElement param) {
        if (auronSocket == null) return;

        JsonObject base = new JsonObject();
        base.addProperty("msg", command);
        base.add("param", param);
        this.auronSocket.send(base.toString());
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
