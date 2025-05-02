package com.jazzkuh.m0nitor.utils.music;

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

		SpotifyConfiguration spotifyConfiguration = new SpotifyConfiguration.Builder()
				.autoReconnect(true)
				.exceptionReconnectDelay(10000L)
				.build();

		this.spotifyAPI.initializeAsync(spotifyConfiguration);
		this.provider = provider;
	}

	public void playPause() {
		if (!spotifyAPI.isConnected()) return;

		Concurrency.async().execute(() -> {
			switch (this.provider) {
				case SPOTIFY -> spotifyAPI.pressMediaKey(MediaKey.PLAY_PAUSE);
			}
		});
	}

	public void next() {
		if (!spotifyAPI.isConnected()) return;

		Concurrency.async().execute(() -> {
			switch (this.provider) {
				case SPOTIFY -> spotifyAPI.pressMediaKey(MediaKey.NEXT);
			}
		});
	}

	public void previous() {
		if (!spotifyAPI.isConnected()) return;

		Concurrency.async().execute(() -> {
			switch (this.provider) {
				case SPOTIFY -> spotifyAPI.pressMediaKey(MediaKey.PREV);
			}
		});
	}

	public boolean isPlaying() {
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
