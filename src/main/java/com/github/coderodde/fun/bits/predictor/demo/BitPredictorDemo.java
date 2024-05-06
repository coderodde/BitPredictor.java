package com.github.coderodde.fun.bits.predictor.demo;

import com.github.coderodde.fun.bits.predictor.BitPredictor;
import com.github.coderodde.fun.bits.predictor.impl.FrequencyBitPredictor;
import com.github.coderodde.fun.bits.predictor.impl.MachineLearningBitPredictor;
import com.github.coderodde.fun.bits.predictor.util.Utils;
import java.util.Random;

public final class BitPredictorDemo {
    
    public static void main(final String[] args) {
        final long seed = System.currentTimeMillis();
        final int patternLength = 6;
        final int bitStringLength = 30;
        final Random random = new Random(seed);
        
        System.out.printf("seed = %d\n", seed);
        
        final boolean[] bits = 
                Utils.getRandomWeightedLearningBitString(
                        random, 
                        0.5, 
                        bitStringLength);
        
//        final boolean[] bits = Utils.getBitStringWithPatterns(random, 
//                                                              patternLength, 
//                                                              bitStringLength);
        
        System.out.printf("Bits: %s.\n", toBitString(bits));
        
        final BitPredictor predictor = 
                new MachineLearningBitPredictor(bits, patternLength, random);
        
        System.out.println("<<< Predictor state >>>");
        System.out.println(predictor);
        
        final boolean[] predictedArray = 
                predictor.predictArray(bitStringLength);
        
        System.out.printf(
                "%.4f\n", 
                Utils.getSimilarityPercent(bits, 
                                           predictedArray));
        
        final BitPredictor predictor2 = 
                new FrequencyBitPredictor(bits, random);
        
        final boolean[] predictedArray2 = 
                predictor2.predictArray(bitStringLength);
        
        System.out.printf(
                "%.4f\n", 
                Utils.getSimilarityPercent(bits, 
                                           predictedArray2));
    }

    private static String toBitString(final boolean[] bits) {
        final StringBuilder stringBuilder = new StringBuilder(bits.length);
        
        for (int i = 0; i < bits.length; i++) {
            stringBuilder.append((bits[i] ? "1" : "0"));
        }
        
        return stringBuilder.toString();
    }
}
