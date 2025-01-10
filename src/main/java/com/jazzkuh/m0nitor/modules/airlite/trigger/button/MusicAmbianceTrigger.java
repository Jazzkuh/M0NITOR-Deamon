package com.jazzkuh.m0nitor.modules.airlite.trigger.button;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlButton;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedBlinkSpeed;
import com.jazzkuh.m0nitor.framework.airlite.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.airlite.trigger.TriggerAction;
import com.jazzkuh.m0nitor.utils.image.AccentColorChanger;
import com.jazzkuh.m0nitor.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.m0nitor.utils.lighting.bulb.Bulb;
import com.jazzkuh.m0nitor.utils.lighting.bulb.BulbRegistry;
import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.open.model.track.Image;
import de.labystudio.spotifyapi.open.model.track.OpenTrack;
import lombok.SneakyThrows;

import java.awt.*;
import java.util.List;

public class MusicAmbianceTrigger extends TriggerAction {
	@Override
	@SneakyThrows
	public void process() {
		if (!airliteModule.getEnabledButtons().contains("music_ambiance")) {
			udpModule.writeBlinkingLed(ControlButton.LED_7B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
			airliteModule.getEnabledButtons().add("music_ambiance");

			SpotifyAPI spotifyAPI = Deamon.getInstance().getMusicEngine().getSpotifyAPI();
			OpenTrack openTrack = spotifyAPI.getOpenAPI().requestOpenTrack(spotifyAPI.getTrack());
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
		} else {
			udpModule.writeStaticLed(ControlButton.LED_7B, ControlLedColor.GREEN);
			airliteModule.getEnabledButtons().remove("music_ambiance");

			for (Bulb bulb : BulbRegistry.getBulbsByGroups("studio")) {
				PhilipsWizLightController.setScene(bulb, PhilipsWizLightController.Scene.Sunset, 100);
			}
		}
	}

	@Override
	public void startActions() {
		if (!airliteModule.getEnabledButtons().contains("music_ambiance")) {
			udpModule.writeStaticLed(ControlButton.LED_7B, ControlLedColor.GREEN);
		} else {
			udpModule.writeBlinkingLed(ControlButton.LED_7B, ControlLedColor.RED, ControlLedColor.OFF, ControlLedBlinkSpeed.SLOW);
		}
	}
}
