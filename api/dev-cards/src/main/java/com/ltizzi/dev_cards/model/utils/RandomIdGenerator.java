package com.ltizzi.dev_cards.model.utils;

import java.util.Random;

/**
 * @author Leonardo Terlizzi
 */

public class RandomIdGenerator {

    public long generateRandomLong(){
        long maxValue = 90071992547409L;
        Random random = new Random();
        return Math.abs(random.nextLong() %(maxValue+1));
    }
}
