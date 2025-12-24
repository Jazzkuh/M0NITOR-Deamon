package com.jazzkuh.m0nitor.framework.auron;

import com.google.gson.JsonObject;

public abstract class AuronHandler {
    public abstract boolean shouldProcess(String msg);
    public abstract void process(String msg, JsonObject param);
}
