package com.dictionary.word.slang.services;

import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;

import java.util.*;

public class SlangService {
    private static SlangService instance;
    private final SortedMap<String, List<String>> slangMap;

    private SlangService() {
        if (FileIO.isExists(Constant.Path.SLANG_DICTIONARY)) {
            slangMap = FileIO.readMap(Constant.Path.SLANG_DICTIONARY);
        } else {
            slangMap = FileIO.readMap(Constant.Path.BASE_SLANG_DICTIONARY);
            saveToFile();
        }
    }

    public static synchronized SlangService getInstance() {
        if (instance == null) {
            instance = new SlangService();
        }

        return instance;
    }

    public String[][] getAll() {
        List<String[]> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : slangMap.entrySet()) {
            addEntryToList(results, entry);
        }

        return results.toArray(String[][]::new);
    }

    private void saveToFile() {
        FileIO.writeMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    public String[][] searchBySlang(String slang) {
        if (slang.isBlank()) {
            return getAll();
        }

        SlangHistoryService.getInstance().addSlang(slang);

        var nextLastChar = (char) (slang.charAt(slang.length() - 1) + 1);
        var end = slang.substring(0, slang.length() - 1) + nextLastChar;
        var resultsMap = slangMap.subMap(slang, end);
        var results = new ArrayList<String[]>();

        for (var entry : resultsMap.entrySet()) {
            addEntryToList(results, entry);
        }

        return results.toArray(String[][]::new);
    }

    public String[][] searchByDefinition(String definition) {
        if (definition.isBlank()) {
            return getAll();
        }

        SlangHistoryService.getInstance().addDefinition(definition);

        var results = new ArrayList<String[]>();

        for (var entry : slangMap.entrySet()) {
            int indexOfDefinition = 0;
            for (var def : entry.getValue()) {
                if (def.toLowerCase().contains(definition.toLowerCase())) {
                    results.add(new String[]{
                            String.valueOf(results.size()),
                            entry.getKey(),
                            definition,
                            String.valueOf(indexOfDefinition)
                    });
                }
                indexOfDefinition++;
            }
        }

        return results.toArray(String[][]::new);
    }

    private static void addEntryToList(List<String[]> lst, Map.Entry<String, List<String>> entry) {
        int indexOfDefinition = 0;
        for (var definition : entry.getValue()) {
            lst.add(new String[]{
                    String.valueOf(lst.size()),
                    entry.getKey(),
                    definition,
                    String.valueOf(indexOfDefinition)
            });

            indexOfDefinition++;
        }
    }
}
