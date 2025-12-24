package com.jazzkuh.m0nitor.framework.auron.trigger;

public interface TriggerActionImpl {
    void process();

    default void startActions() {
    }
}
