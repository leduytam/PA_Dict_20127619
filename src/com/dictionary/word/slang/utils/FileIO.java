package com.dictionary.word.slang.utils;

import java.io.*;
import java.util.*;
import java.util.List;

public class FileIO {
    public static SortedMap<String, List<String>> readMap(String filePath) {
        var map = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
        String line = null;

        try (var reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                var tokens = line.split(Constant.Regex.SLANG_DEFINITION_SPLIT_REGEX);

                if (tokens.length != 2 || tokens[0].isBlank()) {
                    continue;
                }

                var slang = tokens[0].trim();
                var definitions = new ArrayList<>(Arrays.asList(tokens[1].split(Constant.Regex.DEFINITION_SPLIT_REGEX)));

                if (map.containsKey(slang)) {
                    var newDefinitions = map.get(slang);
                    newDefinitions.addAll(definitions);
                    map.put(slang, newDefinitions);
                } else {
                    map.put(slang, definitions);
                }
            }

            return map;
        } catch (IOException exception) {
            exception.printStackTrace();
            return new TreeMap<>();
        }
    }

    public static void writeMap(SortedMap<String, List<String>> map, String filePath) {
        try (var writer = new BufferedWriter(new FileWriter(filePath))) {
            for (var entry : map.entrySet()) {
                var slang = entry.getKey();
                var definitions = String.join(Constant.Regex.DEFINITION_JOIN_DELIMITER, entry.getValue());
                writer.write(String.format("%s`%s", slang, definitions));
                writer.newLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static List<String> readHistory(String filePath) {
        var history = new ArrayList<String>();
        String line = null;

        try (var reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                history.add(line);
            }
        } catch (IOException ignored) {
            // If file not found. Do nothing
        }

        return history;
    }

    public static void writeHistory(List<String> histories, String filePath) {
        try (var writer = new BufferedWriter(new FileWriter(filePath))) {
            for (var history : histories) {
                writer.write(history);
                writer.newLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }

    public static String getCurrentDirectoryPath() {
        try {
            return new File(".").getCanonicalPath();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
