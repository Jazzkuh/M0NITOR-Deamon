package com.jazzkuh.m0nitor.modules.udp.generic;

import com.jazzkuh.m0nitor.framework.udp.GenericHandler;
import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import com.jazzkuh.m0nitor.modules.udp.generic.handlers.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GenericProcessor {
    private final UDPModule udpModule;
    private final List<GenericHandler> handlers = new ArrayList<>();

    public GenericProcessor(UDPModule udpModule) {
        this.udpModule = udpModule;

        handlers.add(new FaderHandler());
        handlers.add(new ChannelHandler());
        handlers.add(new CueHandler());
        handlers.add(new MicrophoneHandler());
        handlers.add(new AutoCueHandler());
        handlers.add(new ButtonHandler());
        handlers.add(new VoiceTrackHandler());
    }

    public void process(byte[] data) {
        try {
            byte size = data[2];
            byte cmd = data[3];

            for (GenericHandler handler : handlers) {
                if (!handler.shouldProcess(size, cmd)) continue;
                handler.process(size, cmd, data);
            }
        } catch (Exception exception) {
            udpModule.getLogger().warn("Error while receiving data: {}", exception.getMessage());
        }
    }
}
