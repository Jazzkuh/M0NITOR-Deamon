package com.jazzkuh.m0nitor.framework.airlite.trigger;

public interface TriggerActionImpl {
    void process();

    default void startActions() {
    }
}
