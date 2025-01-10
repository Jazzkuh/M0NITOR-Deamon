package com.jazzkuh.m0nitor.utils.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import com.jazzkuh.m0nitor.utils.image.AccentColorChanger;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.Bulb;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.SpotifyListener;
import de.labystudio.spotifyapi.model.Track;
import de.labystudio.spotifyapi.open.model.track.Image;
import de.labystudio.spotifyapi.open.model.track.OpenTrack;
import lombok.SneakyThrows;

import java.awt.*;
import java.util.List;

public class SpotifyEventListener implements SpotifyListener {
    private final UDPModule udpModule = Deamon.getModuleManager().get(UDPModule.class);
    private final AirliteModule airliteModule = Deamon.getModuleManager().get(AirliteModule.class);

    @Override
    public void onConnect() {
    }

    @Override
    @SneakyThrows
    public void onTrackChanged(Track track) {
        if (!airliteModule.getEnabledButtons().contains("music_ambiance")) return;

        SpotifyAPI spotifyAPI = Deamon.getInstance().getMusicEngine().getSpotifyAPI();
        OpenTrack openTrack = spotifyAPI.getOpenAPI().requestOpenTrack(track);
        if (openTrack != null) {
            Image image = openTrack.album.images.getFirst();
            if (image == null) return;

            String imageUrl = image.url;
            if (imageUrl == null) return;

            AccentColorChanger.getMostProminentColor(imageUrl, color -> {
                List<Bulb> bulbs = BulbRegistry.getBulbsByGroup("studio");
                Color accentColor = Color.decode(color);

                // check which rgb value of the accent color is the highest and set that to 255
                int max = Math.max(accentColor.getRed(), Math.max(accentColor.getGreen(), accentColor.getBlue()));
                Color newAccentColor = new Color(accentColor.getRed() * 255 / max, accentColor.getGreen() * 255 / max, accentColor.getBlue() * 255 / max);

                for (Bulb bulb : bulbs) {
                    PhilipsWizLightController.setRGBColor(bulb, newAccentColor, 100);
                }
            });
        }
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
