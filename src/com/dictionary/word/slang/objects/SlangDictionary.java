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

    /**
     * Load the slang dictionary from file
     */
    private void loadData() {
        FileIO.tryToCreateDirectory(Constant.Path.DATA_DIRECTORY);

        if (FileIO.isFileExists(Constant.Path.SLANG_DICTIONARY)) {
            slangMap = FileIO.readSlangMap(Constant.Path.SLANG_DICTIONARY);
        } else if (FileIO.isFileExists(Constant.Path.BASE_SLANG_DICTIONARY)) {
            slangMap = FileIO.readSlangMap(Constant.Path.BASE_SLANG_DICTIONARY);
            FileIO.writeSlangMap(slangMap, Constant.Path.SLANG_DICTIONARY);
        } else {
            String message = "No database found!!!\n" +
                    "Please add your database with name base-slang-dictionary.dat\n" +
                    "To path: " +
                    Constant.Path.DATA_DIRECTORY;
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }
    }

    /**
     * @return The number of unique slang words in the dictionary
     */
    public int getUniqueSize() {
        return slangMap.size();
    }

    /**
     * @return All the data in the dictionary with the format:
     * [index, slang, definition, definitionIndex]
     * ...
     */
    public String[][] getAll() {
        List<String[]> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : slangMap.entrySet()) {
            addEntryToList(results, entry);
        }

        return results.toArray(String[][]::new);
    }

    /**
     * Add new slang to the dictionary
     * If the slang already exists, add the definition to the list of definitions
     * Otherwise, add the new slang to the dictionary
     * @param slang The slang word
     * @param definition The definition of the slang word
     */
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
        FileIO.writeSlangMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    /**
     * Add new slang to the dictionary
     * If the slang already exists, the definitions will be replaced with the new one
     * Otherwise, the new slang will be added to the dictionary
     * @param slang The slang word
     * @param definition The definition of the slang word
     */
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
        FileIO.writeSlangMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    public boolean isExists(String slang) {
        return slangMap.containsKey(slang);
    }

    /**
     * Update the slang definition
     * @param slang The slang word
     * @param definition The definition of the slang word
     * @param definitionIndex The index of the definition
     */
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
        FileIO.writeSlangMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    /**
     * @param slang The slang word
     * @param definitionIndex The index of definition
     * @return True if remove successfully, otherwise return false
     */
    public boolean remove(String slang, int definitionIndex) {
        if (slang.isBlank()) {
            return false;
        }

        List<String> definitions = slangMap.get(slang);

        if (definitions == null || definitionIndex < 0 || definitionIndex >= definitions.size()) {
            return false;
        }

        definitions.remove(definitionIndex);

        if (definitions.isEmpty()) {
            slangMap.remove(slang);
        } else {
            slangMap.put(slang, definitions);
        }

        FileIO.writeSlangMap(slangMap, Constant.Path.SLANG_DICTIONARY);

        return true;
    }

    /**
     * @param slang The slang word to search
     * @return The results that starts with the given slang word
     */
    public String[][] searchBySlang(String slang) {
        if (slang.isBlank()) {
            return getAll();
        }

        SlangHistory.getInstance().addSlang(slang);

        char nextLastChar = (char) (slang.charAt(slang.length() - 1) + 1);
        String end = slang.substring(0, slang.length() - 1) + nextLastChar;
        SortedMap<String, List<String>> resultsMap = slangMap.subMap(slang, end);
        List<String[]> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : resultsMap.entrySet()) {
            addEntryToList(results, entry);
        }

        return results.toArray(String[][]::new);
    }

    /**
     *
     * @param definition The definition to search
     * @return The results that contains the definition
     */
    public String[][] searchByDefinition(String definition) {
        if (definition.isBlank()) {
            return getAll();
        }

        SlangHistory.getInstance().addDefinition(definition);

        List<String[]> results = new ArrayList<>();

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

    /**
     * Reset slang dictionary to base slang dictionary and save to file
     */
    public void reset() {
        slangMap = FileIO.readSlangMap(Constant.Path.BASE_SLANG_DICTIONARY);
        FileIO.writeSlangMap(slangMap, Constant.Path.SLANG_DICTIONARY);
    }

    /**
     * Add entry to list with format:
     * Entry[slang, List[definitions]] -> String[index, slang, definition, definitionIndex]
     * @param lst List<String[]> to add entry to
     * @param entry Entry<String, List<String>> to add
     */
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

    /**
     * If the current size of unique slang words in the dataset is less than the given size,
     * then the return list will be null
     * @return A random list of unique slang words
     */
    public List<String[]> generateRandomSlangList(int size) {
        if (size <= 0 || size > slangMap.size()) {
            return null;
        }

        List<String[]> results = new ArrayList<>();
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

    /**
     * If the current size of unique slang in the dataset is less than the given size / 4,
     * then the return list will be null
     * @param size The number of quizzes to be generated.
     * @param isReverse If true, the quiz will be in the form of "definition - slang".
     *                  If false, the quiz will be in the form of "slang - definition".
     * @return A list of quizzes.
     */
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

        List<String> randomOptions = new ArrayList<>();
        List<SlangQuiz> quizzes = new ArrayList<>();

        for (String[] slang : randomSlangList) {
            randomOptions.add(slang[optionIndex]);
        }

        Collections.shuffle(randomOptions);

        for (int i = 0, j = 0; i < size; i++) {
            String question = randomSlangList.get(i)[questionIndex];
            String correctOption = randomSlangList.get(i)[optionIndex];
            List<String> incorrectOptions = new ArrayList<>();

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
