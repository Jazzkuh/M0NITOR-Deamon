package com.jazzkuh.m0nitor.utils.hue;

import com.jazzkuh.m0nitor.modules.web.socket.LightSocketHandler;
import com.jazzkuh.m0nitor.utils.FileUtils;
import io.github.zeroone3010.yahueapi.HueBridge;
import io.github.zeroone3010.yahueapi.discovery.HueBridgeDiscoveryService;
import io.github.zeroone3010.yahueapi.v2.*;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class HueController {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Hue hue;

    protected io.github.zeroone3010.yahueapi.Hue oldHue;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Map<List<Light>, Group> lights = new HashMap<>();

    @SneakyThrows
    public HueController() {
        String bridgeIp = FileUtils.readFileFromResources("hue-bridge-ip.txt");
        String bridgeToken = FileUtils.readFileFromResources("hue-bridge-token.txt");

        setHue(new Hue(bridgeIp, bridgeToken));
        oldHue = new io.github.zeroone3010.yahueapi.Hue(bridgeIp, bridgeToken);

        if (hue != null) {
            for (Group room : hue.getRooms().values()) {
                lights.put(room.getLights().stream().toList(), room);
            }
        }

        hue.subscribeToEvents(new LightSocketHandler());
    }


//    private void test() {
//        Room room = hue.getRooms().stream().toList().getFirst();
//        for (Light light : room.getLights()) {
//            light.setBrightness(100);
//            light.setState(State.builder().color(Color.of(255, 0, 0)).on());
//        }
//    }

    @Nullable
    public Light getLightByName(String lightName) {
        return lights.keySet().stream().flatMap(List::stream).filter(light -> light.getName().equals(lightName)).toList().getFirst();
    }

    public int getLightBrightness(Light light) {
        return oldHue.getAllLights().getLights().stream().filter(l -> l.getName().equals(light.getName())).toList().getFirst().getState().getBri();
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
