package com.dictionary.word.slang.utils;

import java.sql.Timestamp;

public class Constant {
    public static class Path {
        public static final String CURRENT_DIRECTORY = FileIO.getCurrentDirectoryPath();
        public static final String ERROR_LOG = String.format("%s/errors-%d.log", CURRENT_DIRECTORY, (new Timestamp(System.currentTimeMillis())).getTime());
        public static final String SLANG = String.format("%s/data/dict.dat", CURRENT_DIRECTORY);
        public static final String SLANG_BASE = String.format("%s/data/slang-base.dat", CURRENT_DIRECTORY);
        public static final String SLANG_HISTORY = String.format("%s/data/slang-history.dat", CURRENT_DIRECTORY);
        public static final String DEFINITION_HISTORY = String.format("%s/data/definition-history.dat", CURRENT_DIRECTORY);
    }
}
