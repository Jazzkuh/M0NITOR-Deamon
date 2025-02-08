package com.jazzkuh.m0nitor.utils.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import de.labystudio.spotifyapi.SpotifyListener;
import de.labystudio.spotifyapi.model.Track;
import lombok.SneakyThrows;

public class SpotifyEventListener implements SpotifyListener {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);

    @Override
    public void onConnect() {
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
        if (isPlaying) {
            udpModule.writeStaticLed(ControlButton.LED_1A, ControlLedColor.GREEN);
        } else {
            udpModule.writeStaticLed(ControlButton.LED_1A, ControlLedColor.RED);
        }
    }

    @Override
    public void onSync() {
    }

    @Override
    public void onDisconnect(Exception exception) {
    }
}
