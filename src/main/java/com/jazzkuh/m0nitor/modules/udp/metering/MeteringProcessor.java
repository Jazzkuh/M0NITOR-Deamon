package com.jazzkuh.m0nitor.modules.udp.metering;

import com.jazzkuh.m0nitor.modules.udp.UDPModule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MeteringProcessor {
    private final UDPModule udpModule;

    private final Map<Integer, Double> bits = Map.of(
            0, 0D,
            1, 0.25D,
            2, 0.5D,
            3, 0.75D
    );

    public void process(byte[] data) {
        try {
            // byte 0 and 1 are the AirLite header
            byte size = data[2];
            byte cmd = data[3];

            if (size == (byte) 0x08 && cmd == (byte) 0xF0) {
                byte data0 = data[4];
                byte data1 = data[5];
                byte data2 = data[6];
                byte data3 = data[7];
                byte data4 = data[8];
                byte data5 = data[9];

                // Masks
                int valueMask = 0x3F;      // Mask for bits [5:0]
                int decPartMask = 0x03;     // Mask for bits [1:0]

                // Extracting data and calculating metering values
                double progL = (data0 & valueMask) + (bits.get(data0 & decPartMask));
                double progR = (data1 & valueMask) + (bits.get(data1 & decPartMask));
                double phonesL = (data2 & valueMask) + (bits.get(data2 & decPartMask));
                double phonesR = (data3 & valueMask) + (bits.get(data3 & decPartMask));
                double crmL = (data4 & valueMask) + (bits.get(data4 & decPartMask));
                double crmR = (data5 & valueMask) + (bits.get(data5 & decPartMask));

                // Creating the map of metering values
                udpModule.setMeteringValues(Map.of(
                        "program_left", progL,
                        "program_right", progR,
                        "phones_left", phonesL,
                        "phones_right", phonesR,
                        "crm_left", crmL,
                        "crm_right", crmR
                ));
            }
        } catch (Exception exception) {
            udpModule.getLogger().warn("Error while receiving data: {}", exception.getMessage());
        }
    }
}
