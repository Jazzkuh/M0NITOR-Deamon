package com.jazzkuh.m0nitor.framework.airlite.button;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ControlLedBlinkSpeed {
    SLOW((byte) 0x00),
    NORMAL((byte) 0x01),
    FAST((byte) 0x02);

    private final byte data;
}
