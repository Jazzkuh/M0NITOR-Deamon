package com.jazzkuh.m0nitor.modules.airlite.tasks;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.modulemanager.generic.handlers.tasks.TaskInfo;
import core.GLA;
import de.labystudio.spotifyapi.SpotifyAPI;
import de.labystudio.spotifyapi.model.Track;
import genius.SongSearch;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.TimerTask;

@TaskInfo(delay = 1000, period = 1000, repeating = true)
@AllArgsConstructor
public class LyricsFetchTask extends TimerTask {
    private final AirliteModule airliteModule;

    @Override
    @SneakyThrows
    public void run() {
        GLA gla = new GLA();

        SpotifyAPI spotifyAPI = Deamon.getInstance().getMusicEngine().getSpotifyAPI();
        Track currentTrack = spotifyAPI.getTrack();
        if (currentTrack == null) return;
        if (airliteModule.getLyricsCache().containsKey(currentTrack.getName() + " " + currentTrack.getArtist())) return;

        LinkedList<SongSearch.Hit> hits = gla.search(currentTrack.getName() + " " + currentTrack.getArtist()).getHits();
        if (hits.isEmpty()) return;

        String lyrics = hits.getFirst().fetchLyrics();
        if (lyrics == null) return;

        airliteModule.getLyricsCache().put(currentTrack.getName() + " " + currentTrack.getArtist(), lyrics);
    }
}
