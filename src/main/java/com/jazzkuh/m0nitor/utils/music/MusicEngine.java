package com.jazzkuh.m0nitor.utils.music;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.utils.Concurrency;
import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.SpotifyAPIFactory;
import de.labystudio.spotifyapi.config.SpotifyConfiguration;
import de.labystudio.spotifyapi.model.MediaKey;
import lombok.Getter;

@Getter
public class MusicEngine {
	private SpotifyAPI spotifyAPI;
	private MusicEngineProvider provider;

	public MusicEngine(MusicEngineProvider provider) {
		this.spotifyAPI = SpotifyAPIFactory.create();
		this.initializeSpotifyAPI();
		this.provider = provider;
	}

	public void initializeSpotifyAPI() {
		this.initializeSpotifyAPI(ReconnectDelay.DEFAULT, true);
	}

	public void initializeSpotifyAPI(ReconnectDelay reconnectDelay, boolean ignoreInitialized) {
		if (this.spotifyAPI.isInitialized()) {
			if (!ignoreInitialized) {
				return;
			}

			this.spotifyAPI.stop();
		}

		this.spotifyAPI.initializeAsync(
				new de.labystudio.spotifyapi.config.SpotifyConfiguration.Builder()
						.autoReconnect(false)
						.exceptionReconnectDelay(reconnectDelay.getDelay())
						.build()
		);
	}

	public void pressMediaKey(MediaKey mediaKey) {
		try {
			this.spotifyAPI.pressMediaKey(mediaKey);
		} catch (IllegalArgumentException e) {
			Deamon.getLogger().error("Failed to press media key", e);
		}
	}

	public boolean isPlaying() {
		if (!spotifyAPI.isConnected()) return false;

		switch (this.provider) {
			case SPOTIFY -> {
				return spotifyAPI.isPlaying();
			}
		}

		return false;
	}

	public enum MusicEngineProvider {
		SPOTIFY,
		APPLE_MUSIC
	}
}
