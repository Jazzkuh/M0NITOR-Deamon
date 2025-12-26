package com.jazzkuh.m0nitor.modules.auron.processing;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;
import com.jazzkuh.m0nitor.modules.auron.processing.handlers.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AuronProcessor {
    private final List<AuronHandler> handlers = new ArrayList<>();

    public AuronProcessor() {
        handlers.add(new ModuleHandler());
        handlers.add(new FaderHandler());
        handlers.add(new CueHandler());
        handlers.add(new MicrophoneHandler());
        handlers.add(new ButtonHandler());
        handlers.add(new InitialStatesHandler());
        handlers.add(new MeteringHandler());
        handlers.add(new ModuleActiveHandler());
        handlers.add(new ModuleMeteringHandler());
    }

    public void process(String msg, JsonObject param) {
        try {
            for (AuronHandler handler : handlers) {
                if (!handler.shouldProcess(msg)) continue;
                handler.process(msg, param);
            }
        } catch (Exception exception) {
            System.out.println("Error while receiving data: " + exception.getMessage());
        }
    }
}
