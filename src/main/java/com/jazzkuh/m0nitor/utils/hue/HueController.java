package com.jazzkuh.m0nitor.utils.hue;

import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.utils.Concurrency;
import com.jazzkuh.m0nitor.utils.FileUtils;
import io.github.zeroone3010.yahueapi.HueBridgeConnectionBuilder;
import io.github.zeroone3010.yahueapi.v2.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HueController {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Hue hue;

    protected io.github.zeroone3010.yahueapi.Hue oldHue;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Map<List<Light>, Group> lights = new HashMap<>();

    @Setter
    @Getter
    private String lastScene = "Osaka";

    @SneakyThrows
    public HueController() {
        Concurrency.async().execute(() -> {
            String bridgeIp = FileUtils.readFileFromResources("hue-bridge-ip.txt");
            String bridgeToken = FileUtils.readFileFromResources("hue-bridge-token.txt");

            setHue(new Hue(bridgeIp, bridgeToken));
            oldHue = new io.github.zeroone3010.yahueapi.Hue(bridgeIp, bridgeToken);

            if (hue != null) {
                for (Group room : hue.getRooms().values()) {
                    lights.put(room.getLights().stream().toList(), room);
                }
            }
        });
    }

//    private void test() {
//        Room room = hue.getRooms().stream().toList().getFirst();
//        for (Light light : room.getLights()) {
//            light.setBrightness(100);
//            light.setState(State.builder().color(Color.of(255, 0, 0)).on());
//        }
//    }

    private void fetchToken(String bridgeIp) {
        CompletableFuture<String> apiKey = new HueBridgeConnectionBuilder(bridgeIp).initializeApiConnection("M0NITOR");
        apiKey.whenComplete((key, err) -> {
            Deamon.getLogger().info("Store this API key for future use: " + key);
        });
    }

    @Nullable
    public Light getLightByName(String lightName) {
        return lights.keySet().stream().flatMap(List::stream).filter(light -> light.getName().equals(lightName)).toList().getFirst();
    }

    public int getLightBrightness(Light light) {
        return oldHue.getAllLights().getLights().stream().filter(l -> l.getName().equals(light.getName())).toList().getFirst().getState().getBri();
    }

    public void setLightColor(Light light, Color color) {
        light.setState(new UpdateState().color(io.github.zeroone3010.yahueapi.Color.of(color.getRed(), color.getGreen(), color.getBlue())).brightness(100).on());
    }

    public void setLightState(Light light, boolean on) {
        light.setState(new UpdateState().on(on));
    }

    public void setLightBrightness(Light light, int brightness) {
        light.setBrightness(brightness);
    }

    public void setScene(Group room, String sceneName) {
        room.getSceneByName(sceneName).ifPresent(Scene::activate);
    }

    @Nullable
    public Group getRoomByName(String roomName) {
        return hue.getRoomByName(roomName).orElse(null);
    }
}
