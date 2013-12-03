package com.echo8.bprmf.utils;

import java.util.Random;

public class RandomUtils {
    private static Random instance;

    private RandomUtils() {}

    private static void init() {
        instance = new Random();
    }

    public static void init(long seed) {
        instance = new Random(seed);
    }

    public static Random getInstance() {
        if (instance == null) {
            init();
        }

        return instance;
    }
}
