package com.jazzkuh.m0nitor.framework.airlite.button;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ControlLedColor {
    OFF((byte) 0x00),
    RED((byte) 0x01),
    GREEN((byte) 0x02);

    private final byte data;
}
