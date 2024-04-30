package com.github.coderodde.fun.bits.predictor.demo;

import com.github.coderodde.fun.bits.predictor.BitPredictor;
import com.github.coderodde.fun.bits.predictor.FrequencyBitPredictor;
import com.github.coderodde.fun.bits.predictor.MachineLearningBitPredictor;
import java.util.Random;

public final class BitPredictorDemo {
    
    private static final int LEARNING_BIT_STRING_LENGTH = 1_000_000;
    private static final int LEARNING_PATTERN_MAXMUM_LENGTH = 13;
    
    public static void main(final String[] args) {
        final long seed = System.currentTimeMillis();
        final Random random = new Random(seed);
        System.out.printf("Seed = %d.\n", seed);
        
        long start = System.currentTimeMillis();
        final boolean[] learningBitString = getRandomLearningBitString(random);
        long end = System.currentTimeMillis();
        
        System.out.printf(
                "Constructed the learning bit string in %d milliseconds.\n",
                end - start);
        
        start = System.currentTimeMillis();
        
        final BitPredictor frequencyBitPredictor = 
                new FrequencyBitPredictor(learningBitString, random);
        
        end = System.currentTimeMillis();
        
        System.out.printf(
                "Learned the FrequencyBitPredictor in %d milliseconds.\n", 
                end - start);
        
        start = System.currentTimeMillis();
        
        final BitPredictor machineLearningBitPredictor = 
                new MachineLearningBitPredictor(
                        learningBitString,
                        LEARNING_PATTERN_MAXMUM_LENGTH, 
                        random);
        
        end = System.currentTimeMillis();
        
        System.out.printf(
                "Learned the MachineLearningBitPredictor in %d milliseconds.\n", 
                end - start);
    }
    
    private static boolean[] getRandomLearningBitString(final Random random) {
        final boolean[] bitString = new boolean[LEARNING_BIT_STRING_LENGTH];
        
        for (int i = 0; i < bitString.length; i++) {
            bitString[i] = random.nextBoolean();
        }
        
        return bitString;
    }
}
