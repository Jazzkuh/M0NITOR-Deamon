package com.jazzkuh.m0nitor.modules.auron.processing.handlers;

import com.google.gson.JsonObject;
import com.jazzkuh.m0nitor.Deamon;
import com.jazzkuh.m0nitor.framework.auron.AuronHandler;
import com.jazzkuh.m0nitor.modules.auron.AuronModule;

import java.util.HashMap;
import java.util.Map;

public class MeteringHandler extends AuronHandler {
    private final AuronModule auronModule = Deamon.getModuleManager().get(AuronModule.class);

    @Override
    public boolean shouldProcess(String msg) {
        return msg.equalsIgnoreCase("on_metering:master");
    }
    @Override
    public void process(String msg, JsonObject param) {
        double progL = param.getAsJsonObject("pgm").get("left").getAsDouble();
        double progR = param.getAsJsonObject("pgm").get("right").getAsDouble();
        double auxL = param.getAsJsonObject("aux").get("left").getAsDouble();
        double auxR = param.getAsJsonObject("aux").get("right").getAsDouble();
        double phonesL = param.getAsJsonObject("pfl").get("left").getAsDouble();
        double phonesR = param.getAsJsonObject("pfl").get("right").getAsDouble();
        double masterL = param.getAsJsonObject("master").get("left").getAsDouble();
        double masterR = param.getAsJsonObject("master").get("right").getAsDouble();
        double crmL = param.getAsJsonObject("crm").get("left").getAsDouble();
        double crmR = param.getAsJsonObject("crm").get("right").getAsDouble();
        double subL = param.getAsJsonObject("sub").get("left").getAsDouble();
        double subR = param.getAsJsonObject("sub").get("right").getAsDouble();
        double studioL = param.getAsJsonObject("studio").get("left").getAsDouble();
        double studioR = param.getAsJsonObject("studio").get("right").getAsDouble();
        double vtL = param.getAsJsonObject("vt").get("left").getAsDouble();
        double vtR = param.getAsJsonObject("vt").get("right").getAsDouble();

        Map<String, Double> meteringValues = new HashMap<>();

        meteringValues.put("program_left", progL);
        meteringValues.put("program_right", progR);
        meteringValues.put("phones_left", phonesL);
        meteringValues.put("phones_right", phonesR);
        meteringValues.put("crm_left", crmL);
        meteringValues.put("crm_right", crmR);
        meteringValues.put("aux_left", auxL);
        meteringValues.put("aux_right", auxR);
        meteringValues.put("master_left", masterL);
        meteringValues.put("master_right", masterR);
        meteringValues.put("sub_left", subL);
        meteringValues.put("sub_right", subR);
        meteringValues.put("studio_left", studioL);
        meteringValues.put("studio_right", studioR);
        meteringValues.put("vt_left", vtL);
        meteringValues.put("vt_right", vtR);

        auronModule.setMeteringValues(meteringValues);
    }
}
