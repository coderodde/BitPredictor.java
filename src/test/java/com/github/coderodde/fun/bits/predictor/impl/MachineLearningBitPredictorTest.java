package com.github.coderodde.fun.bits.predictor.impl;

import com.github.coderodde.fun.bits.predictor.BitPredictor;
import com.github.coderodde.fun.bits.predictor.util.Utils;
import java.util.Random;
import org.junit.Test;

public final class MachineLearningBitPredictorTest {
    
    @Test
    public void predict1() {
        final int patternLength = 2;
        final int bitStringLength = 6;
        final Random random = new Random(13L);
        final boolean[] bits = Utils.getBitStringWithPatterns(random, 
                                                              patternLength, 
                                                              bitStringLength);
        final BitPredictor predictor = 
                new MachineLearningBitPredictor(bits, patternLength, random);
        
        final boolean[] predictedArray = 
                predictor.predictArray(bitStringLength);
        
        System.out.printf(
                "%.4f\n", 
                Utils.getSimilarityPercent(bits, 
                                           predictedArray));
    }
}
