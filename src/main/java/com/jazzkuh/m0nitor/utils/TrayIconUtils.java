package com.jazzkuh.m0nitor.utils;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.button.ButtonTrigger;
import com.jazzkuh.m0nitor.framework.auron.button.ControlButton;
import com.jazzkuh.m0nitor.framework.auron.button.ControlLedColor;
import com.jazzkuh.m0nitor.framework.auron.trigger.TriggerAction;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.modules.auron.registry.ButtonTriggerRegistry;
import com.jazzkuh.m0nitor.utils.logging.LogViewerWindow;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.awt.*;
import javax.swing.SwingUtilities;

@UtilityClass
public class TrayIconUtils {
    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);
    private static LogViewerWindow logViewerWindow;

    @SneakyThrows
    public static void create(Image image) {
        if (!SystemTray.isSupported()) return;
        SystemTray systemTray = SystemTray.getSystemTray();

        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem logsItem = new MenuItem("View Logs");
        logsItem.addActionListener(actionEvent -> SwingUtilities.invokeLater(() -> {
            if (logViewerWindow == null) logViewerWindow = new LogViewerWindow();
            logViewerWindow.setVisible(true);
        }));
        trayPopupMenu.add(logsItem);

        MenuItem repairItem = new MenuItem("Repair");
        repairItem.addActionListener(actionEvent -> {
            Deamon.getInstance().getMusicEngine().initializeSpotifyAPI();

            EmberLedUtil.writeAll(auronModule, ControlLedColor.OFF);
            for (ButtonTrigger buttonTrigger : ButtonTriggerRegistry.getTriggers().keySet()) {
                ControlButton controlButton = buttonTrigger.getControlButton();
                EmberLedUtil.writeStaticLed(auronModule, controlButton, controlButton.getDefaultLedColor());

                TriggerAction triggerAction = ButtonTriggerRegistry.getAction(buttonTrigger);
                if (triggerAction == null) continue;
                triggerAction.startActions();
            }
        });
        trayPopupMenu.add(repairItem);

        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener(actionEvent -> {
            Deamon.getInstance().onShutdown();
            System.exit(0);
        });
        trayPopupMenu.add(closeItem);

        TrayIcon trayIcon = new TrayIcon(image, "M0NITOR Deamon", trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        systemTray.add(trayIcon);
    }
}
