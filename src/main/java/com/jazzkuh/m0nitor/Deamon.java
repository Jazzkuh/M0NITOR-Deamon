package com.jazzkuh.m0nitor;

import ch.qos.logback.classic.Level;
import com.jazzkuh.m0nitor.utils.TrayIconUtils;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import com.jazzkuh.m0nitor.utils.music.MusicEngine;
import com.jazzkuh.m0nitor.utils.music.SpotifyEventListener;
import com.jazzkuh.modulemanager.generic.GenericModuleManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URL;

public final class Deamon {

    @Getter @Setter(AccessLevel.PRIVATE)
    private static Logger logger;

    @Getter @Setter(AccessLevel.PRIVATE)
    private static Deamon instance;

    @Getter @Setter(AccessLevel.PRIVATE)
    private static GenericModuleManager moduleManager;

    @Getter
    private final MusicEngine musicEngine;

    @Getter
    private final HueController hueController;

    @SneakyThrows
    public Deamon() {
        setInstance(this);
        setLogger(LoggerFactory.getLogger(getClass().getSimpleName()));
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);

        URL imageUrl = this.getClass().getClassLoader().getResource("app.png");
        Image image = Toolkit.getDefaultToolkit().createImage(imageUrl);
        TrayIconUtils.create(image);

        setModuleManager(new GenericModuleManager(logger));
        moduleManager.scanModules(getClass());
        moduleManager.load();

        this.musicEngine = new MusicEngine(MusicEngine.MusicEngineProvider.SPOTIFY);
        this.musicEngine.getSpotifyAPI().registerListener(new SpotifyEventListener());
        this.hueController = new HueController();

        moduleManager.enable();
    }

    public void onShutdown() {
        moduleManager.disable();
    }
}