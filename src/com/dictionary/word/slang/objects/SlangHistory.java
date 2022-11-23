package com.dictionary.word.slang.objects;

import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;

import java.util.List;

public class SlangHistory {
    private static SlangHistory instance;
    private final List<String> slangHistory;
    private final List<String> definitionHistory;

    private SlangHistory() {
        slangHistory = FileIO.readLines(Constant.Path.SLANG_HISTORY);
        definitionHistory = FileIO.readLines(Constant.Path.DEFINITION_HISTORY);
    }

    public static synchronized SlangHistory getInstance() {
        if (instance == null) {
            instance = new SlangHistory();
        }

        return instance;
    }

    public List<String> getSlangHistory() {
        return slangHistory;
    }

    public List<String> getDefinitionHistory() {
        return definitionHistory;
    }

    /**
     * Add a new slang word to the beginning of the slang history
     * If the history already contains the slang, remove it and add it to the beginning of the list
     * @param slang The slang word to add
     */
    public void addSlang(String slang) {
        if (slang.isBlank()) {
            return;
        }

        slangHistory.remove(slang);
        slangHistory.add(0, slang);
        FileIO.writeLines(slangHistory, Constant.Path.SLANG_HISTORY);
    }

    /**
     * Add a new definition to the beginning of the definition history
     * If the history already contains the definition, remove it and add it to the beginning of the list
     * @param definition The definition to add
     */
    public void addDefinition(String definition) {
        if (definition.isBlank()) {
            return;
        }

        definitionHistory.remove(definition);
        definitionHistory.add(0, definition);
        FileIO.writeLines(definitionHistory, Constant.Path.DEFINITION_HISTORY);
    }

    /**
     * Clear slang history and save to file
     */
    public void clearSlangHistory() {
        slangHistory.clear();
        saveSlangHistoryToFile();
    }

    /**
     * Clear definition history and save to file
     */
    public void clearDefinitionHistory() {
        definitionHistory.clear();
        saveDefinitionHistoryToFile();
    }

    /**
     * Save slang history to file
     */
    private void saveSlangHistoryToFile() {
        FileIO.writeLines(slangHistory, Constant.Path.SLANG_HISTORY);
    }

    /**
     * Save definition history to file
     */
    private void saveDefinitionHistoryToFile() {
        FileIO.writeLines(definitionHistory, Constant.Path.DEFINITION_HISTORY);
    }
}
