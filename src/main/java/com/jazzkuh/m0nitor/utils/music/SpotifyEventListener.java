package com.jazzkuh.m0nitor.utils.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.utils.EmberLedUtil;
import de.labystudio.spotifyapi.SpotifyListener;
import de.labystudio.spotifyapi.model.Track;
import lombok.SneakyThrows;

public class SpotifyEventListener implements SpotifyListener {
    private final MusicEngine musicEngine;

    public SpotifyEventListener(MusicEngine musicEngine) {
        this.musicEngine = musicEngine;
    }

    @Override
    public void onConnect() {
        System.out.println("Connected to Spotify");
    }

    @Override
    @SneakyThrows
    public void onTrackChanged(Track track) {
    }

    @Override
    public void onPositionChanged(int position) {
    }

    @Override
    public void onPlayBackChanged(boolean isPlaying) {
        AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);
        if (isPlaying) {
            EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_1A, ControlLedColor.GREEN);
        } else {
            EmberLedUtil.writeStaticLed(auronModule, ControlButton.LED_1A, ControlLedColor.RED);
        }
    }

    @Override
    public void onSync() {
    }

    @Override
    public void onDisconnect(Exception exception) {
        System.out.println("Disconnected");

        ReconnectDelay next = ReconnectDelay.DEFAULT.next();
        this.musicEngine.initializeSpotifyAPI(next, true);
    }
}
