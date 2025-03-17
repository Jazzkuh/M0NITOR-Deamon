package com.jazzkuh.m0nitor.utils.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AccentColorChanger {
    // Gets the most prominent color from an image URL
    public static void getMostProminentColor(String imageUrl, ColorCallback callback) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();

                int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
                java.util.List<Color> rgbList = buildRgb(pixels);

                String hexColor = findColor(rgbList, false);

                if (hexColor == null) {
                    hexColor = findColor(rgbList, true);
                }

                callback.onColorDetected(hexColor);
            }
        } catch (Exception e) {
            System.err.println("Image load error: " + e.getMessage());
            callback.onColorDetected(null);
        }
    }

    // Finds the most prominent color in a list of RGB values
    public static String findColor(java.util.List<Color> rgbList, boolean skipFilters) {
        Map<String, Integer> colorCount = new HashMap<>();
        String maxColor = "";
        int maxCount = 0;

        for (Color color : rgbList) {
            if (!skipFilters && (isTooDark(color) || isTooCloseToWhite(color))) {
                continue;
            }

            String rgbKey = color.getRed() + "," + color.getGreen() + "," + color.getBlue();
            colorCount.put(rgbKey, colorCount.getOrDefault(rgbKey, 0) + 1);

            if (colorCount.get(rgbKey) > maxCount) {
                maxColor = rgbKey;
                maxCount = colorCount.get(rgbKey);
            }
        }

        if (!maxColor.isEmpty()) {
            String[] rgb = maxColor.split(",");
            return rgbToHex(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
        }

        return null;
    }

    // Builds a list of RGB values from pixel data
    public static java.util.List<Color> buildRgb(int[] pixels) {
        java.util.List<Color> rgbValues = new java.util.ArrayList<>();

        for (int pixel : pixels) {
            Color color = new Color(pixel, true);
            rgbValues.add(color);
        }

        return rgbValues;
    }

    // Converts RGB to Hex
    public static String rgbToHex(int r, int g, int b) {
        return String.format("#%02x%02x%02x", r, g, b);
    }

    // Checks if a color is too dark
    public static boolean isTooDark(Color color) {
        double brightness = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
        return brightness < 100;
    }

    // Checks if a color is too close to white
    public static boolean isTooCloseToWhite(Color color) {
        int threshold = 200;
        return color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold;
    }

    // Callback interface for handling color detection
    public interface ColorCallback {
        void onColorDetected(String hexColor);
    }
}
