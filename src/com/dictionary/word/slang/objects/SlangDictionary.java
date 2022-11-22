package com.dictionary.word.slang.objects;

import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;
import com.dictionary.word.slang.utils.Generator;

import javax.swing.*;
import java.util.*;

public class SlangDictionary {
    private static SlangDictionary instance;
    private SortedMap<String, List<String>> slangMap;

    private SlangDictionary() {
        loadData();
    }

    public static synchronized SlangDictionary getInstance() {
        if (instance == null) {
            instance = new SlangDictionary();
        }

        return instance;
    }

    private void loadData() {
        FileIO.tryToCreateDirectory(Constant.Path.DATA_DIRECTORY);

        if (FileIO.isFileExists(Constant.Path.SLANG_DICTIONARY)) {
            slangMap = FileIO.readMap(Constant.Path.SLANG_DICTIONARY);
        } else if (FileIO.isFileExists(Constant.Path.BASE_SLANG_DICTIONARY)) {
            slangMap = FileIO.readMap(Constant.Path.BASE_SLANG_DICTIONARY);
            FileIO.writeMap(slangMap, Constant.Path.SLANG_DICTIONARY);
        } else {
            String message = "No database found!!!\n" +
                    "Please add your database with name base-slang-dictionary.dat\n" +
                    "To path: " +
                    Constant.Path.DATA_DIRECTORY;
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }
    }

    public int getUniqueSize() {
        return slangMap.size();
    }

    public String[][] getAll() {
        List<String[]> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : slangMap.entrySet()) {
            addEntryToList(results, entry);
        }

        return results.toArray(String[][]::new);
    }

    public void addNew(String slang, String definition) {
        if (slang.isBlank() || definition.isBlank()) {
            return;
        }

        List<String> definitions = slangMap.get(slang);

        if (definitions == null) {
            definitions = new ArrayList<>();
        }

        definitions.add(definition);
        slangMap.put(slang, definitions);
        FileIO.writeMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    public void addOverwrite(String slang, String definition) {
        if (slang.isBlank() || definition.isBlank()) {
            return;
        }

        List<String> definitions = slangMap.get(slang);

        if (definitions == null) {
            definitions = new ArrayList<>();
        }

        definitions.clear();
        definitions.add(definition);
        slangMap.put(slang, definitions);
        FileIO.writeMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    public boolean isExists(String slang) {
        return slangMap.get(slang) != null;
    }

    public void set(String slang, String definition, int definitionIndex) {
        if (slang.isBlank() || definition.isBlank()) {
            return;
        }

        List<String> definitions = slangMap.get(slang);

        if (definitions == null || definitionIndex < 0 || definitionIndex >= definitions.size()) {
            return;
        }

        definitions.set(definitionIndex, definition);
        slangMap.put(slang, definitions);
        FileIO.writeMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    public boolean remove(String slang, int definitionIndex) {
        if (slang.isBlank()) {
            return false;
        }

        List<String> definitions = slangMap.get(slang);

        if (definitions == null || definitionIndex < 0 || definitionIndex >= definitions.size()) {
            return false;
        }

        definitions.remove(definitionIndex);
        slangMap.put(slang, definitions);
        FileIO.writeMap(slangMap, Constant.Path.SLANG_DICTIONARY);

        return true;
    }

    public String[][] searchBySlang(String slang) {
        if (slang.isBlank()) {
            return getAll();
        }

        SlangHistory.getInstance().addSlang(slang);

        char nextLastChar = (char) (slang.charAt(slang.length() - 1) + 1);
        String end = slang.substring(0, slang.length() - 1) + nextLastChar;
        SortedMap<String, List<String>> resultsMap = slangMap.subMap(slang, end);
        ArrayList<String[]> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : resultsMap.entrySet()) {
            addEntryToList(results, entry);
        }

        return results.toArray(String[][]::new);
    }

    public String[][] searchByDefinition(String definition) {
        if (definition.isBlank()) {
            return getAll();
        }

        SlangHistory.getInstance().addDefinition(definition);

        ArrayList<String[]> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : slangMap.entrySet()) {
            int indexOfDefinition = 0;
            for (String def : entry.getValue()) {
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

    public void reset() {
        slangMap = FileIO.readMap(Constant.Path.BASE_SLANG_DICTIONARY);
        FileIO.writeMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    private static void addEntryToList(List<String[]> lst, Map.Entry<String, List<String>> entry) {
        int indexOfDefinition = 0;
        for (String definition : entry.getValue()) {
            lst.add(new String[]{
                    String.valueOf(lst.size()),
                    entry.getKey(),
                    definition,
                    String.valueOf(indexOfDefinition)
            });

            indexOfDefinition++;
        }
    }

    public List<String[]> generateRandomSlangList(int size) {
        if (size <= 0 || size > slangMap.size()) {
            return null;
        }

        ArrayList<String[]> results = new ArrayList<>();
        List<Integer> indices = Generator.randDistinctIntArray(0, slangMap.size() - 1, size);

        int i = 0;
        int currentIndicesIndex = 0;

        for (Map.Entry<String, List<String>> entry : slangMap.entrySet()) {
            if (i == indices.get(currentIndicesIndex)) {
                results.add(new String[]{
                        entry.getKey(),
                        entry.getValue().get(Generator.randInt(0, entry.getValue().size() - 1))
                });

                currentIndicesIndex++;

                if (currentIndicesIndex == indices.size()) {
                    break;
                }
            }

            i++;
        }

        return results;
    }

    public List<SlangQuiz> generateRandomQuizzes(int size, boolean isReverse) {
        int questionIndex = 0;
        int optionIndex = 1;

        if (isReverse) {
            questionIndex = 1;
            optionIndex = 0;
        }

        List<String[]> randomSlangList = generateRandomSlangList(size * 4);

        if (randomSlangList == null) {
            return null;
        }

        ArrayList<String> randomOptions = new ArrayList<>();
        ArrayList<SlangQuiz> quizzes = new ArrayList<>();

        for (String[] slang : randomSlangList) {
            randomOptions.add(slang[optionIndex]);
        }

        Collections.shuffle(randomOptions);

        for (int i = 0, j = 0; i < size; i++) {
            String question = randomSlangList.get(i)[questionIndex];
            String correctOption = randomSlangList.get(i)[optionIndex];
            ArrayList<String> incorrectOptions = new ArrayList<String>();

            for (int k = 0; k < 3; k++) {
                if (correctOption.equals(randomOptions.get(j))) {
                    k--;
                    j++;
                    continue;
                }

                incorrectOptions.add(randomOptions.get(j++));

                if (j == randomOptions.size()) {
                    j = 0;
                }
            }

            quizzes.add(new SlangQuiz(question, correctOption, incorrectOptions));
        }

        return quizzes;
    }
}
