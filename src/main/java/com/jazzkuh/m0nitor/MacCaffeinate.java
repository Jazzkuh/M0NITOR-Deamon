package com.jazzkuh.m0nitor;

public final class MacCaffeinate {
    private static Process caffeinateProcess;

    public static void start() {
        if (!isMac()) return;

        try {
            caffeinateProcess = new ProcessBuilder(
                    "/usr/bin/caffeinate",
                    "-i", // prevent idle sleep
                    "-s"  // prevent system sleep
            ).start();

            System.out.println("Caffeinate started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (caffeinateProcess != null) {
            caffeinateProcess.destroy();
        }
    }

    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
