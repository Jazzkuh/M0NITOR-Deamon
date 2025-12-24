package com.jazzkuh.m0nitor.modules.auron.tasks;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import com.jazzkuh.modulemanager.generic.handlers.tasks.TaskInfo;
import io.github.zeroone3010.yahueapi.v2.Group;
import lombok.RequiredArgsConstructor;

import java.util.TimerTask;

@RequiredArgsConstructor
@TaskInfo(period = 570, delay = 1000, repeating = true)
public class FlashingTask extends TimerTask {
    private final AuronModule auronModule;

    @Override
    public void run() {
        if (!auronModule.getEnabledButtons().contains("flashing")) return;

        HueController hueController = Deamon.getInstance().getHueController();
        Group room = hueController.getRoomByName("Studio");

        if (room == null) return;

        int index = auronModule.getFlashingSceneIndex();
        String scene = (index % 2 == 0) ? "Flashing-1" : "Flashing-2";

        hueController.setScene(room, scene);
        auronModule.incrementFlashingSceneIndex();
    }
}