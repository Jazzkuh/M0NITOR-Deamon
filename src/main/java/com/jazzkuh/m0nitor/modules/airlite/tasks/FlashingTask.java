package com.jazzkuh.m0nitor.modules.airlite.tasks;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.modules.airlite.AirliteModule;
import com.jazzkuh.m0nitor.utils.hue.HueController;
import com.jazzkuh.modulemanager.generic.handlers.tasks.TaskInfo;
import io.github.zeroone3010.yahueapi.v2.Group;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.TimerTask;

@RequiredArgsConstructor
@TaskInfo(period = 550, delay = 1000, repeating = true)
public class FlashingTask extends TimerTask {
    private final AirliteModule airliteModule;

    @Override
    public void run() {
        HueController hueController = Deamon.getInstance().getHueController();
        Group room = hueController.getRoomByName("Studio");

        if (!airliteModule.getEnabledButtons().contains("flashing") || room == null) return;

        int index = airliteModule.getFlashingSceneIndex();
        String scene = (index % 2 == 0) ? "Flashing-1" : "Flashing-2";

        hueController.setScene(room, scene);
        airliteModule.incrementFlashingSceneIndex();
    }
}