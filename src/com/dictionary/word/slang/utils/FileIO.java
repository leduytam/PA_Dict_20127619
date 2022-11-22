package com.dictionary.word.slang.utils;

import java.io.*;
import java.util.*;
import java.util.List;

public class FileIO {
    public static SortedMap<String, List<String>> readMap(String filePath) {
        TreeMap<String, List<String>> map = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
        String line = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(Constant.Regex.SLANG_DEFINITION_SPLIT_REGEX);

                if (tokens.length != 2 || tokens[0].isBlank()) {
                    continue;
                }

                String slang = tokens[0].trim();
                ArrayList<String> definitions = new ArrayList<>(Arrays.asList(tokens[1].split(Constant.Regex.DEFINITION_SPLIT_REGEX)));

                if (map.containsKey(slang)) {
                    List<String> newDefinitions = map.get(slang);
                    newDefinitions.addAll(definitions);
                    map.put(slang, newDefinitions);
                } else {
                    map.put(slang, definitions);
                }
            }

            return map;
        } catch (IOException exception) {
            ErrorLogger.severe(exception);
            return new TreeMap<>();
        }
    }

    public static void writeMap(SortedMap<String, List<String>> map, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String slang = entry.getKey();
                String definitions = String.join(Constant.Regex.DEFINITION_JOIN_DELIMITER, entry.getValue());
                writer.write(String.format("%s`%s", slang, definitions));
                writer.newLine();
            }
        } catch (IOException exception) {
            ErrorLogger.severe(exception);
        }
    }

    public static List<String> readHistory(String filePath) {
        ArrayList<String> history = new ArrayList<String>();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                history.add(line);
            }
        } catch (IOException ignored) {}

        return history;
    }

    public static void writeHistory(List<String> histories, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String history : histories) {
                writer.write(history);
                writer.newLine();
            }
        } catch (IOException exception) {
            ErrorLogger.severe(exception);
        }
    }

    public static List<Integer> readScores(String filePath) {
        List<Integer> scores = new ArrayList<>();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                try {
                    scores.add(Integer.parseInt(line));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException ignored) {}

        return scores;
    }

    public static void writeScores(List<Integer> scores, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Integer score : scores) {
                writer.write(score.toString());
                writer.newLine();
            }
        } catch (IOException exception) {
            ErrorLogger.severe(exception);
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
            throw new RuntimeException();
        }
    }
}
