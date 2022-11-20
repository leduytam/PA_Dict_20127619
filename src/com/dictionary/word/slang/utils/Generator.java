package com.dictionary.word.slang.utils;

import java.util.List;
import java.util.Random;

public class Generator {
    private static final Random random = new Random();

    public static int randInt(int min, int max) {
        return random.nextInt(min, max + 1);
    }

    public static List<Integer> randDistinctIntArray(int min, int max, int size) {
        return random.ints(min, max + 1).boxed().distinct().limit(size).sorted().toList();
    }
}
