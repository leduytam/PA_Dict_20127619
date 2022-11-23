package com.dictionary.word.slang.utils;

import java.io.*;
import java.util.*;
import java.util.List;

public class FileIO {
    /**
     * @param filePath the path of file
     * @return A slang map which key is the slang word and value is list of definitions.
     *         Each line of the file is a slang word and its definitions.
     *         If 2 lines have the same slang word, the definitions will be added to the list of definitions.
     *         If the line is invalid, it will be ignored.
     *         If the file is not exist, return empty slang map.
     */
    public static SortedMap<String, List<String>> readSlangMap(String filePath) {
        TreeMap<String, List<String>> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split(Constant.Regex.SLANG_DEFINITION_SPLIT_REGEX);

                if (tokens.length != 2 || tokens[0].isBlank()) {
                    continue;
                }

                String slang = tokens[0];
                List<String> definitions = new ArrayList<>(
                        Arrays.asList(tokens[1].split(Constant.Regex.DEFINITION_SPLIT_REGEX))
                );

                if (map.containsKey(slang)) {
                    List<String> newDefinitions = map.get(slang);
                    newDefinitions.addAll(definitions);
                    map.put(slang, newDefinitions);
                } else {
                    map.put(slang, definitions);
                }
            }
        } catch (IOException exception) {
            ErrorLogger.severe(exception);
        }

        return map;
    }

    /**
     * Write a slang map to a file in the format of "slang`definition1| definition2| ..." line by line.
     * @param map The map to write.
     * @param filePath The path to the file.
     */
    public static void writeSlangMap(SortedMap<String, List<String>> map, String filePath) {
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

    /**
     * Reads a file and returns a list of strings.
     * Each string is a line in the file.
     * The lines are trimmed.
     * Empty lines are ignored.
     * If the file does not exist, an empty list is returned.
     * @param filePath The path to the file to be read.
     * @return A list of strings.
     */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                lines.add(line.trim());
            }
        } catch (IOException ignored) {}

        return lines;
    }

    /**
     * Write history to file.
     * @param lines List of histories to write.
     * @param filePath The path of the file to write to.
     */
    public static void writeLines(List<String> lines, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException exception) {
            ErrorLogger.severe(exception);
        }
    }

    /**
     * @param filePath The path of the file to be read.
     * @return The list of integers in the file.
     *         Each integer is a line in the file.
     *         Ignore invalid lines.
     *         If the file does not exist, return an empty list.
     */
    public static List<Integer> readIntegers(String filePath) {
        List<Integer> integers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                try {
                    integers.add(Integer.parseInt(line));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException ignored) {}

        return integers;
    }

    /**
     * Write a list of integers to a file.
     * Each integer is a line in the file.
     * @param integers List of integers.
     * @param filePath The path of the file.
     */
    public static void writeIntegers(List<Integer> integers, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Integer integer : integers) {
                writer.write(integer.toString());
                writer.newLine();
            }
        } catch (IOException exception) {
            ErrorLogger.severe(exception);
        }
    }

    /**
     * @param filePath The path of the file to be checked.
     * @return True if the file exists, false otherwise.
     */
    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }

    /**
     * Create the directory if it does not exist.
     * @param directoryPath The path of the directory.
     */
    public static void tryToCreateDirectory(String directoryPath) {
        File dir = new File(directoryPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * @return The current working directory of the application.
     */
    public static String getCurrentDirectoryPath() {
        try {
            if (Constant.ENV.IS_DEVELOPMENT_MODE) {
                // use this line when run or debug
                return new File(".").getCanonicalPath();
            }

            // use this line when build artifact
             return new File(FileIO.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }
}
