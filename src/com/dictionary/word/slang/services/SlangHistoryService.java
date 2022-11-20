package com.dictionary.word.slang.services;

import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;

import java.util.List;

public class SlangHistoryService {
    private static SlangHistoryService instance;
    private final List<String> slangHistory;
    private final List<String> definitionHistory;

    private SlangHistoryService() {
        slangHistory = FileIO.readHistory(Constant.Path.SLANG_HISTORY);
        definitionHistory = FileIO.readHistory(Constant.Path.DEFINITION_HISTORY);
    }

    public static synchronized SlangHistoryService getInstance() {
        if (instance == null) {
            instance = new SlangHistoryService();
        }

        return instance;
    }

    public void addSlang(String slang) {
        slangHistory.remove(slang);
        slangHistory.add(0, slang);
        FileIO.writeHistory(slangHistory, Constant.Path.SLANG_HISTORY);
    }

    public void addDefinition(String definition) {
        definitionHistory.remove(definition);
        definitionHistory.add(0, definition);
        FileIO.writeHistory(definitionHistory, Constant.Path.DEFINITION_HISTORY);
    }

    public List<String> getSlangHistory() {
        return slangHistory;
    }

    public List<String> getDefinitionHistory() {
        return definitionHistory;
    }

    public void clearSlangHistory() {
        slangHistory.clear();
        saveSlangHistoryToFile();
    }

    public void clearDefinitionHistory() {
        definitionHistory.clear();
        saveDefinitionHistoryToFile();
    }

    private void saveSlangHistoryToFile() {
        FileIO.writeHistory(slangHistory, Constant.Path.SLANG_HISTORY);
    }

    private void saveDefinitionHistoryToFile() {
        FileIO.writeHistory(definitionHistory, Constant.Path.DEFINITION_HISTORY);
    }
}
