package com.dictionary.word.slang.utils;

import java.sql.Timestamp;

public class Constant {
    public static class Path {
        public static final String CURRENT_DIRECTORY = FileIO.getCurrentDirectoryPath();
        public static final String DATA_DIRECTORY = String.format("%s/data", CURRENT_DIRECTORY);
        public static final String ERROR_LOG = String.format("%s/errors-%d.log", CURRENT_DIRECTORY, (new Timestamp(System.currentTimeMillis())).getTime());
        public static final String SLANG_DICTIONARY = String.format("%s/slang-dictionary.dat", DATA_DIRECTORY);
        public static final String BASE_SLANG_DICTIONARY = String.format("%s/base-slang-dictionary.dat", DATA_DIRECTORY);
        public static final String SLANG_HISTORY = String.format("%s/slang-history.dat", DATA_DIRECTORY);
        public static final String DEFINITION_HISTORY = String.format("%s/definition-history.dat", DATA_DIRECTORY);
        public static final String SCORES = String.format("%s/scores.dat", DATA_DIRECTORY);
    }

    public static class Regex {
        public final static String SLANG_DEFINITION_SPLIT_REGEX = "`";
        public final static String DEFINITION_SPLIT_REGEX = "\\| ";
        public final static String DEFINITION_JOIN_DELIMITER = "| ";
    }

    public static class View {
        public static final String[] TABLE_COLUMN_NAMES = {"#", "Slang", "Definition", "Definition Index"};
        public static final String[] SLANG_TYPES = {"Slang", "Definition"};
    }

    public static class ENV {
        public static final boolean IS_DEVELOPMENT_MODE = true;
    }
}
