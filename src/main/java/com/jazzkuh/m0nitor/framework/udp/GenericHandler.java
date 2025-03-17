package com.jazzkuh.m0nitor.framework.udp;

public abstract class GenericHandler {
    public abstract boolean shouldProcess(byte size, byte cmd);
    public abstract void process(byte size, byte cmd, byte[] data);
}
