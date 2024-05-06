package com.github.coderodde.fun.bits.predictor.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public final class Utils {
    
    public static void checkIsPositiveValue(final int value, 
                                            final String exceptionMessage) {
        if (value < 1) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
    
    public static double getSimilarityPercent(final boolean[] bitString1, 
                                              final boolean[] bitString2) {
        Objects.requireNonNull(bitString1,
                               "The first input bit string is null.");
        
        Objects.requireNonNull(bitString2,
                               "The second input bit string is null.");
        
        if (bitString1.length != bitString2.length) {
            final String exceptionMessage =
                    String.format("Bit string length mismatch: %d vs. %d.", 
                                  bitString1.length, 
                                  bitString2.length);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
        
        int mismatches = 0;
        
        for (int i = 0; i < bitString1.length; i++) {
            final boolean bit1 = bitString1[i];
            final boolean bit2 = bitString2[i];
            
            if (bit1 != bit2) {
                mismatches++;
            }
        }
        
        final double ratio = (double)(mismatches) / (double)(bitString1.length);
        return ratio * 100.0; // Convert to percents.
    }
    
    public static boolean[] 
            getRandomWeightedLearningBitString(
                    final Random random,
                    final double weight,
                    final int learningBitStringLength) {
                
        final boolean[] bitString = new boolean[learningBitStringLength];
        
        for (int i = 0; i < bitString.length; i++) {
            bitString[i] = random.nextDouble() < weight;
        }
        
        return bitString;
    }
            
    public static boolean[] 
        getBitStringWithPatterns(final Random random,
                                 final int patternLength,
                                 final int learningBitStringLength) {
            
        final boolean[] bitString = new boolean[learningBitStringLength];
        
        // Initialize the prefix:
        for (int i = 0;
                 i < Math.min(patternLength, learningBitStringLength);
                 i++) {
            
            bitString[i] = random.nextBoolean();
        }
        
        final Map<BitStringView, Boolean> patternMap =
                getPatternMap(random,
                              patternLength,
                              bitString);
        
        for (int i = patternLength; 
                 i < bitString.length; 
                 i++) {
            
            final BitStringView bitStringView = 
                    new BitStringView(
                            bitString, 
                            i - patternLength, 
                            patternLength);
            
            bitString[i] = patternMap.get(bitStringView);
        }
        
        return bitString;
    }
    
    private static Map<BitStringView, Boolean> 
        getPatternMap(final Random random, 
                      final int patternLength,
                      final boolean[] bitString) {
            
        final boolean[] pattern = new boolean[patternLength];
        final Map<BitStringView, Boolean> map = 
                new HashMap<>(getMapCapacity(patternLength));
        
        do {
            final BitStringView bitStringView =
                    new BitStringView(
                            Arrays.copyOf(
                                    pattern, 
                                    pattern.length));
            
            map.put(bitStringView, getMostProbableBoolean(random, 
                                                          bitString,
                                                          bitStringView));
        } 
        while (incrementPattern(pattern));
        
        return map;
    }
        
    private static boolean getMostProbableBoolean(final Random random,
                                                  final boolean[] bitString,
                                                  final BitStringView pattern) {
        final BitFrequencyDistribution distribution = 
                new BitFrequencyDistribution();
        
        for (int startIndex = 0; 
                 startIndex < bitString.length - pattern.length(); 
                 startIndex++) {
            if (matchesBitString(bitString, 
                                 startIndex,
                                 pattern)
                    && startIndex < bitString.length - pattern.length() - 1) {
                final boolean successorBit =
                        bitString[startIndex + pattern.length()];
                
                if (successorBit) {
                    distribution.inc1();
                } else {
                    distribution.inc0();
                }
            }
        }
        
        return distribution.sample(random);
    }
    
    private static boolean matchesBitString(final boolean[] bitString,
                                            final int startIndex,
                                            final BitStringView pattern) {
        for (int i = startIndex; i < startIndex + pattern.length(); i++) {
            if (bitString[i] != pattern.get(i - startIndex)) {
                return false;
            }
        }
        
        return true;
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
    
    private static int getMapCapacity(final int patternLength) {
        return toPower(2, patternLength + 1) - 1;
    }
        
    private static int toPower(int base, int exponent) {
        int result = 1;
        
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        
        return result;
    }
}
