package com.dictionary.word.slang.utils;

import java.io.File;

public class FileIO {
    public static String getCurrentDirectoryPath() {
        try {
            return new File(".").getCanonicalPath();
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }
}
