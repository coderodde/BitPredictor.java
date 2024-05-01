package com.github.coderodde.fun.bits.predictor.demo;

import com.github.coderodde.fun.bits.predictor.BitPredictor;
import com.github.coderodde.fun.bits.predictor.BitStringView;
import com.github.coderodde.fun.bits.predictor.FrequencyBitPredictor;
import com.github.coderodde.fun.bits.predictor.MachineLearningBitPredictor;
import com.github.coderodde.fun.bits.predictor.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class BitPredictorDemo {
    
    private static final int LEARNING_BIT_STRING_LENGTH = 1_000_000;
    private static final int PREDICTED_BIT_STRING_LENGTH =
            LEARNING_BIT_STRING_LENGTH;
    
    private static final int PATTERN_LENGTH = 6;
    
    private static final int LEARNING_PATTERN_MAXMUM_LENGTH = 20;
    
    public static void main(final String[] args) {
        final long seed = System.currentTimeMillis();
        final Random random = new Random(seed);
        System.out.printf("Seed = %d.\n", seed);
        
        long start = System.currentTimeMillis();
        final boolean[] learningBitString =
                getRandomWeightedLearningBitString(random, 0.75);
        
//        final boolean[] learningBitString = getRandomUniformLearningBitString(random);
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
        
//        System.out.println("<<< ML predictor state >>>");
//        System.out.println(machineLearningBitPredictor);
        
        start = System.currentTimeMillis();
        final boolean[] predictedArray1 = 
                frequencyBitPredictor.predictArray(PREDICTED_BIT_STRING_LENGTH);
        end = System.currentTimeMillis();
        
        System.out.printf("%s predicted in %d milliseconds.\n",
                          frequencyBitPredictor.getClass().getSimpleName(),
                          end - start);
        
        start = System.currentTimeMillis();
        
        final boolean[] predictedArray2 = 
                machineLearningBitPredictor
                        .predictArray(PREDICTED_BIT_STRING_LENGTH);
        
        end = System.currentTimeMillis();
        
        System.out.printf("%s predicted in %d milliseconds.\n",
                          machineLearningBitPredictor.getClass()
                                                     .getSimpleName(),
                          end - start);
        
        System.out.printf("Similarity by %s is %.4f percent.\n",
                          frequencyBitPredictor.getClass().getSimpleName(),
                          Utils.getSimilarityPercent(predictedArray1, 
                                                     learningBitString));
        
        System.out.printf("Similarity by %s is %.4f percent.\n",
                          machineLearningBitPredictor.getClass()
                                                     .getSimpleName(),
                          Utils.getSimilarityPercent(predictedArray2, 
                                                     learningBitString));
    }
    
    private static boolean[] 
        getRandomUniformLearningBitString(final Random random) {
        
        final boolean[] bitString = new boolean[LEARNING_BIT_STRING_LENGTH];
        
        for (int i = 0; i < bitString.length; i++) {
            bitString[i] = random.nextBoolean();
        }
        
        return bitString;
    }
        
    private static boolean[] 
            getRandomWeightedLearningBitString(final Random random,
                                               final double weight) {
        final boolean[] bitString = new boolean[LEARNING_BIT_STRING_LENGTH];
        
        for (int i = 0; i < bitString.length; i++) {
            bitString[i] = random.nextDouble() < weight;
        }
        
        return bitString;
    }
            
    private static boolean[] getBitStringWithPatterns(final Random random) {
        final boolean[] bitString = new boolean[LEARNING_BIT_STRING_LENGTH];
        
        // Initialize the prefix:
        for (int i = 0; i < PATTERN_LENGTH; i++) {
            bitString[i] = random.nextBoolean();
        }
        
        final Map<BitStringView, Boolean> patternMap = getPatternMap(random);
        
        for (int i = PATTERN_LENGTH; i < bitString.length; i++) {
            final BitStringView bitStringView = 
                    new BitStringView(
                            bitString, 
                            i - PATTERN_LENGTH, 
                            PATTERN_LENGTH);
            
            bitString[i] = patternMap.get(bitStringView);
        }
        
        return bitString;
    }
    
    private static Map<BitStringView, Boolean> 
        getPatternMap(final Random random) {
        final boolean[] pattern = new boolean[PATTERN_LENGTH];
        final Map<BitStringView, Boolean> map = 
                new HashMap<>(toPower(2, PATTERN_LENGTH));
        
        do {
            final BitStringView bitStringView = new BitStringView(pattern);
            map.put(bitStringView, random.nextBoolean());
        } 
        while (incrementPattern(pattern));
        
        return map;
    }
        
    private static boolean incrementPattern(final boolean[] pattern) {
        for (int i = 0; i < pattern.length; i++) {
            if (pattern[i] == false) {
                pattern[i] = true;
                
                for (int j = 0; j < i; j++) {
                    pattern[j] = false;
                }
                
                return true;
            }
        }
        
        return false;
    }
        
    private static int toPower(int base, int exponent) {
        int result = 1;
        
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        
        return result;
    }
}
