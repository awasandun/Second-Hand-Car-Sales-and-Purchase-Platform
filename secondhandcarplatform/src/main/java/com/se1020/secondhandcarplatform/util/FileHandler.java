package com.se1020.secondhandcarplatform.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    // Generic method to save a list of objects to a file
    public static <T> void saveToFile(List<T> items, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : items) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generic method to read lines from a file
    public static List<String> readFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        // Create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
