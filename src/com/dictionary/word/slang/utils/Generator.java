package com.dictionary.word.slang.utils;

import java.util.List;
import java.util.Random;

public class Generator {
    private static final Random random = new Random();

    /**
     * @param min Inclusive
     * @param max Inclusive
     * @return A random integer between min and max
     */
    public static int randInt(int min, int max) {
        return random.nextInt(min, max + 1);
    }

    /**
     * @param min Inclusive
     * @param max Inclusive
     * @param size Size of the list
     * @return List of random numbers between min and max which sorted in ascending order and has no duplicates
     */
    public static List<Integer> randDistinctIntArray(int min, int max, int size) {
        return random.ints(min, max + 1).boxed().distinct().limit(size).sorted().toList();
    }
}
