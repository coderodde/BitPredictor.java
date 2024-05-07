package com.github.coderodde.fun.bits.predictor.demo;

import com.github.coderodde.fun.bits.predictor.BitPredictor;
import com.github.coderodde.fun.bits.predictor.impl.NaiveFrequencyBitPredictor;
import com.github.coderodde.fun.bits.predictor.impl.MachineLearningBitPredictor;
import com.github.coderodde.fun.bits.predictor.util.Utils;
import java.util.Random;

public final class BitPredictorDemo {
    
    public static void main(final String[] args) {
        bruteforceDemo();
    }
    
    private static void bruteforceDemo() {
        final Random random = new Random();
        
        int improvedCount = 0;
        int equalsCount = 0;
        int totalCount = 0;
        
        for (int patternLength = 1; patternLength <= 10; patternLength++) {
            for (int length = 100; length <= 1_000; length += 100) {
                
                final boolean[] bitString =
                        Utils.getRandomWeightedLearningBitString(
                                random,
                                0.7, 
                                patternLength);
                
                final BitPredictor predictor1 = 
                        new NaiveFrequencyBitPredictor(bitString, random);
                
                final BitPredictor predictor2 = 
                        new MachineLearningBitPredictor(
                                bitString, 
                                patternLength,
                                random);
                
                final boolean[] predictedArray1 = 
                        predictor1.predictArray(bitString.length);
                
                final boolean[] predictedArray2 = 
                        predictor2.predictArray(bitString.length);
                
                final double similarityPercent1 = 
                        Utils.getSimilarityPercent(bitString, predictedArray1);
                
                final double similarityPercent2 =
                        Utils.getSimilarityPercent(bitString, predictedArray2);
                
                final boolean improved = 
                        similarityPercent1 < 
                        similarityPercent2;
                
                if (similarityPercent1 == similarityPercent2) {
                    equalsCount++;
                }
                
                System.out.printf(
                        "--- Pattern length: %d, length = %d, improved = %b\n", 
                        patternLength, 
                        length,
                        improved);
                
                System.out.printf(
                        "Similarity FBP:  %.2f %%.\n", 
                        similarityPercent1);
                
                System.out.printf(
                        "Similarity MLBP: %.2f %%.\n\n", 
                        similarityPercent2);
                
                if (improved) {
                    improvedCount++;
                }
                
                totalCount++;
            }
        }
        
        System.out.printf(
                "Improved: %d, equal: %d, run: %d, not-improved: %d, " + 
                        "improved ratio: %.3f, equal ratio: %.3f, " + 
                        "equal/better ratio: %.3f.\n",
                
                improvedCount,
                equalsCount,
                totalCount, 
                totalCount - improvedCount - equalsCount,
                (double)(improvedCount) / (double)(totalCount),
                (double)(equalsCount) / (double)(totalCount),
                (double)(equalsCount + improvedCount) / (double)(totalCount));
        
    }
}
