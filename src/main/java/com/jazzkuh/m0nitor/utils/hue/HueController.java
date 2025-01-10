package com.jazzkuh.m0nitor.utils.hue;

import com.jazzkuh.m0nitor.utils.FileUtils;
import io.github.zeroone3010.yahueapi.*;
import io.github.zeroone3010.yahueapi.discovery.HueBridgeDiscoveryService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.Future;

public class HueController {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Hue hue;

    @SneakyThrows
    public HueController() {
        String bridgeToken = FileUtils.readFileFromResources("hue-bridge-token.txt");

        Future<List<HueBridge>> bridgesFuture = new HueBridgeDiscoveryService().discoverBridges(bridge -> System.out.println("Bridge found: " + bridge));
        List<HueBridge> bridges = bridgesFuture.get();
        if (!bridges.isEmpty()) {
            String bridgeIp = bridges.getFirst().getIp();
            setHue(new Hue(bridgeIp, bridgeToken));
        }
    }

//    private void test() {
//        Room room = hue.getRooms().stream().toList().getFirst();
//        for (Light light : room.getLights()) {
//            light.setBrightness(100);
//            light.setState(State.builder().color(Color.of(255, 0, 0)).on());
//        }
//    }

    public void setScene(Room room, String sceneName) {
        room.getSceneByName(sceneName).ifPresent(Scene::activate);
    }

    public Room getRoomByName(String roomName) {
        return hue.getGroupsOfType(GroupType.ROOM).stream().filter(room -> room.getName().equals(roomName)).toList().getFirst();
    }
}
