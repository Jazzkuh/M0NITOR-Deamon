package com.jazzkuh.m0nitor.utils.music;

import com.jazzkuh.m0nitor.utils.Concurrency;
import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.SpotifyAPIFactory;
import de.labystudio.spotifyapi.model.MediaKey;
import lombok.Getter;

@Getter
public class MusicEngine {
	private SpotifyAPI spotifyAPI;
	private MusicEngineProvider provider;

	public MusicEngine(MusicEngineProvider provider) {
		this.spotifyAPI = SpotifyAPIFactory.create();
		this.spotifyAPI.initialize();
		this.provider = provider;
	}

	public void playPause() {
		Concurrency.async().execute(() -> {
			switch (this.provider) {
				case SPOTIFY -> spotifyAPI.pressMediaKey(MediaKey.PLAY_PAUSE);
			}
		});
	}

	public void next() {
		Concurrency.async().execute(() -> {
			switch (this.provider) {
				case SPOTIFY -> spotifyAPI.pressMediaKey(MediaKey.NEXT);
			}
		});
	}

	public void previous() {
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
