package com.jazzkuh.m0nitor;

import ch.qos.logback.classic.Level;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import com.jazzkuh.m0nitor.utils.music.SpotifyTokenManager;
import com.jazzkuh.modulemanager.generic.GenericModuleManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Timer;

public final class Deamon {

    @Getter @Setter(AccessLevel.PRIVATE)
    private static Logger logger;

    @Getter @Setter(AccessLevel.PRIVATE)
    private static Deamon instance;

    @Getter @Setter(AccessLevel.PRIVATE)
    private static GenericModuleManager moduleManager;

    @Getter
    private final MusicEngine musicEngine;

    @SneakyThrows
    public Deamon() {
        setInstance(this);
        setLogger(LoggerFactory.getLogger(getClass().getSimpleName()));
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);

        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        URL imageResource = Bootstrap.class.getClassLoader().getResource("app.png");
        java.awt.Image image = defaultToolkit.getImage(imageResource);
        Taskbar.getTaskbar().setIconImage(image);

        setModuleManager(new GenericModuleManager(logger));
        moduleManager.scanModules(getClass());
        moduleManager.load();

        this.musicEngine = new MusicEngine(MusicEngine.MusicEngineProvider.SPOTIFY);

        SpotifyTokenManager spotifyTokenManager = new SpotifyTokenManager();
        spotifyTokenManager.run();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(spotifyTokenManager, 5000, 5000);

        moduleManager.enable();
    }

    public void onShutdown() {
        moduleManager.disable();
    }
}