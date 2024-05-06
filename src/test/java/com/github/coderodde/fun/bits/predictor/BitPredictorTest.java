package com.github.coderodde.fun.bits.predictor;

import com.github.coderodde.fun.bits.predictor.impl.FrequencyBitPredictor;
import com.github.coderodde.fun.bits.predictor.impl.MachineLearningBitPredictor;
import com.github.coderodde.fun.bits.predictor.util.Utils;
import java.util.Random;
import org.junit.Test;

public final class BitPredictorTest {
    
    @Test
    public void test1() {
        final int patternLength = 2;
        final int bitStringLength = 6;
        final Random random = new Random(13L);
//        final boolean[] bits = { true, false, true, true, false, true, true };
        final boolean[] bits = Utils.getBitStringWithPatterns(random, 
                                                              patternLength, 
                                                              bitStringLength);
        
        final BitPredictor predictor1 = new FrequencyBitPredictor(bits, random);
        
        final BitPredictor predictor2 = 
                new MachineLearningBitPredictor(bits, patternLength, random);
        
        long start = System.currentTimeMillis();
        
        final boolean[] predictedArray1 = 
                predictor1.predictArray(bitStringLength);
        
        long end = System.currentTimeMillis();
        
        System.out.println("<<< ML dump >>>");
        System.out.println(predictor2);
        
        System.out.printf(
                "%s in %d milliseconds, similarity = %.4f\n",
                predictor1.getClass().getSimpleName(),
                end - start,
                Utils.getSimilarityPercent(bits, 
                                           predictedArray1));
        
        start = System.currentTimeMillis();
        
        final boolean[] predictedArray2 = 
                predictor2.predictArray(bitStringLength);
        
        end = System.currentTimeMillis();
        
        System.out.printf(
                "%s in %d milliseconds, similarity = %.4f\n",
                predictor2.getClass().getSimpleName(),
                end - start,
                Utils.getSimilarityPercent(bits, 
                                           predictedArray2));
    }
}
