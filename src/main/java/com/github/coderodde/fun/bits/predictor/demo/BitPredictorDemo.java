package com.github.coderodde.fun.bits.predictor.demo;

import com.github.coderodde.fun.bits.predictor.BitPredictor;
import com.github.coderodde.fun.bits.predictor.util.BitStringView;
import com.github.coderodde.fun.bits.predictor.impl.FrequencyBitPredictor;
import com.github.coderodde.fun.bits.predictor.impl.MachineLearningBitPredictor;
import com.github.coderodde.fun.bits.predictor.util.Utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class BitPredictorDemo {
    
    private static final int LEARNING_BIT_STRING_LENGTH = 10;
    private static final int PREDICTED_BIT_STRING_LENGTH =
            LEARNING_BIT_STRING_LENGTH;
    
    private static final int PATTERN_LENGTH = 2;
    
    public static void main(final String[] args) {
        final long seed = 1714559375492L; //System.currentTimeMillis();
        final Random random = new Random(seed);
        System.out.printf("Seed = %d.\n", seed);
        
        long start = System.currentTimeMillis();
        boolean[] learningBitString =
                Utils.getRandomWeightedLearningBitString(
                        random,
                        0.75,
                        LEARNING_BIT_STRING_LENGTH);
        
        long end = System.currentTimeMillis();
        
        System.out.printf(
                "Constructed the learning bit string in %d milliseconds.\n",
                end - start);
        
        System.out.println("<<< Predicting fully random bit strings >>>");
        
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
                        PATTERN_LENGTH, 
                        random);
        
        end = System.currentTimeMillis();
        
        System.out.printf(
                "Learned the MachineLearningBitPredictor in %d milliseconds.\n", 
                end - start);
        
        start = System.currentTimeMillis();
        
        boolean[] predictedArray1 = 
                frequencyBitPredictor.predictArray(PREDICTED_BIT_STRING_LENGTH);
        
        end = System.currentTimeMillis();
        
        System.out.printf("%s predicted in %d milliseconds.\n",
                          frequencyBitPredictor.getClass().getSimpleName(),
                          end - start);
        
        start = System.currentTimeMillis();
        
        boolean[] predictedArray2 = 
                machineLearningBitPredictor
                        .predictArray(PREDICTED_BIT_STRING_LENGTH);
        
        end = System.currentTimeMillis();
        
        System.out.printf("%s predicted in %d milliseconds.\n",
                          machineLearningBitPredictor.getClass()
                                                     .getSimpleName(),
                          end - start);
        
        System.out.println();
        
        System.out.printf("Similarity by %s is %.1f percent.\n",
                          frequencyBitPredictor.getClass().getSimpleName(),
                          Utils.getSimilarityPercent(predictedArray1, 
                                                     learningBitString));
        
        System.out.printf("Similarity by %s is %.1f percent.\n",
                          machineLearningBitPredictor.getClass()
                                                     .getSimpleName(),
                          Utils.getSimilarityPercent(predictedArray2, 
                                                     learningBitString));
        
        System.out.println();
        System.out.println("<<< Predicting bit strings with patterns >>>");
        
        start = System.currentTimeMillis();
        learningBitString = 
                Utils.getBitStringWithPatterns(
                        random, 
                        PATTERN_LENGTH, 
                        LEARNING_BIT_STRING_LENGTH);
        end = System.currentTimeMillis();
        
        System.out.printf(
                "Constructed the patterned learning bit string in " + 
                        "%d milliseconds.\n",
                end - start);
        
        start = System.currentTimeMillis();
        
        predictedArray1 = 
                frequencyBitPredictor.predictArray(learningBitString.length);
        
        end = System.currentTimeMillis();
        
        System.out.printf(
                "%s predicted in %d milliseconds.\n", 
                frequencyBitPredictor.getClass().getSimpleName(), 
                end - start);
        
        start = System.currentTimeMillis();
        
        predictedArray1 = 
                machineLearningBitPredictor
                        .predictArray(learningBitString.length);
        
        end = System.currentTimeMillis();
        
        System.out.printf(
                "%s predicted in %d milliseconds.\n", 
                machineLearningBitPredictor.getClass().getSimpleName(), 
                end - start);
        
        System.out.println();
        
        System.out.printf("Similarity by %s is %.1f percent.\n",
                          frequencyBitPredictor.getClass().getSimpleName(),
                          Utils.getSimilarityPercent(predictedArray1, 
                                                     learningBitString));
        
        System.out.printf("Similarity by %s is %.1f percent.\n",
                          machineLearningBitPredictor.getClass()
                                                     .getSimpleName(),
                          Utils.getSimilarityPercent(predictedArray2, 
                                                     learningBitString));
    }
}
