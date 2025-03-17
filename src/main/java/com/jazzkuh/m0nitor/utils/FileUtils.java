package com.jazzkuh.m0nitor.utils;

import com.jazzkuh.m0nitor.Deamon;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@UtilityClass
public class FileUtils {
    @SneakyThrows
    public static String readFileFromResources(String fileName) {
        InputStream inputStream = Deamon.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new Exception("File not found: " + fileName);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder fileContents = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            fileContents.append(line).append(System.lineSeparator());
        }

        reader.close();
        return fileContents.toString().trim();
    }
}
