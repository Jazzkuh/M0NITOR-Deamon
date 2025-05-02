package com.jazzkuh.m0nitor.utils;

import com.jazzkuh.m0nitor.Deamon;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

@UtilityClass
public class TrayIconUtils {
    @SneakyThrows
    public static void create(Image image) {
        if (!SystemTray.isSupported()) return;
        SystemTray systemTray = SystemTray.getSystemTray();

        PopupMenu trayPopupMenu = new PopupMenu();
        ActionListener listener = actionEvent -> {
            Deamon.getInstance().onShutdown();
            System.exit(0);
        };

        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener(listener);
        trayPopupMenu.add(closeItem);

        TrayIcon trayIcon = new TrayIcon(image, "M0NITOR Deamon", trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        systemTray.add(trayIcon);
    }
}
