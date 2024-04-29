package com.github.coderodde.fun.bits.predictor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * This class implements a 
 * @author rodio
 */
public final class MachineLearningBitPredictor implements BitPredictor {
    
    private static final int MAXIMUM_PATTERN_LENGTH_UPPER_BOUND = 26;
    private static final int DEFAULT_PATTERN_LENGTH_UPPER_BOUND = 10;

    private final boolean[] bits;
    private final int maximumPatternLength;
    private final Random random;
    private final Map<boolean[], BitFrequencies> map = new HashMap<>();
    
    public MachineLearningBitPredictor(final boolean[] bits, 
                                       final int maximumPatternLength,
                                       final Random random) {
        
        this.bits = Objects.requireNonNull(bits, "The input bits is null.");
        this.maximumPatternLength = 
                checkMaximumPatternLength(maximumPatternLength);
        
        this.random = Objects.requireNonNull(random,
                                             "The input Random is null.");
        
        process();
    }
    
    public MachineLearningBitPredictor(final boolean[] bits, 
                                       final Random random) {
        this(bits, DEFAULT_PATTERN_LENGTH_UPPER_BOUND, random);
    }
    
    @Override
    public boolean predict(final boolean[] bitString) {
        final BitFrequencies tentativeBitDistribution = new BitFrequencies();
        
        for (final Map.Entry<boolean[], BitFrequencies> e : map.entrySet()) {
            final boolean[] pattern = e.getKey();
            
            if (pattern.length > bitString.length) {
                // Once here, the current pattern is longer than the input bit
                // string; omit it.
                continue;
            }
            
            final boolean[] patternSuffix = 
                    getPatternSuffix(
                            pattern, 
                            bitString.length);
            
            if (map.containsKey(patternSuffix)) {
                tentativeBitDistribution.add(map.get(patternSuffix));
            }
        }
        
        return tentativeBitDistribution.sample(random);
    }
    
    @Override
    public boolean[] predictArray() {
        final boolean[] predictedArray = new boolean[bits.length];
        
        predictedArray[0] = bits[0];
        
        
        
        return predictedArray;
    }
    
    private static boolean[] getPatternSuffix(final boolean[] pattern, 
                                              final int suffixLength) {
        
        final boolean[] patternSuffix = new boolean[suffixLength];
        
        for (int i = pattern.length - suffixLength, j = 0; 
                 j < suffixLength; 
                 j++, i++) {
            
            patternSuffix[j] = pattern[i];
        }
        
        return patternSuffix;
    }
    
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        
        for (final Map.Entry<boolean[], BitFrequencies> e : map.entrySet()) {
            stringBuilder.append(booleanArrayToBitString(e.getKey()))
                         .append(" ")
                         .append(e.getValue())
                         .append("\n");
        }
        
        // Delete the concluding "\n":
        stringBuilder.deleteCharAt(stringBuilder.length() - 1); 
        return stringBuilder.toString();
    }
    
    private static String booleanArrayToBitString(final boolean[] bits) {
        final StringBuilder stringBuilder = new StringBuilder();
        
        for (final boolean bit : bits) {
            stringBuilder.append((bit ? "1" : "0"));
        }
        
        return stringBuilder.toString();
    }
    
    private int checkMaximumPatternLength(final int maximumPatternLength) {
        if (maximumPatternLength < 1) {
            throw new IllegalArgumentException(
                    String.format(
                            "Maximum pattern length is too small (%d), " + 
                                    "must be at least 1.", 
                            maximumPatternLength));
        }
        
        return Math.min(maximumPatternLength, 
                        Math.min(MAXIMUM_PATTERN_LENGTH_UPPER_BOUND,
                                 bits.length - 1));
    }
    
    private void process() {
        for (int patternLength = 1; 
                 patternLength <= maximumPatternLength; 
                 patternLength++) {
            
            processForPatternLength(patternLength);
        }
    }
    
    private void processForPatternLength(final int patternLength) {
        for (int patternStartIndex = 0; 
                 patternStartIndex < bits.length - patternLength; 
                 patternStartIndex++) {
            
            loadPattern(patternStartIndex, 
                        patternLength);
        }
    }
    
    private void loadPattern(final int patternStartIndex, 
                             final int patternLength) {
        final boolean[] pattern = new boolean[patternLength];
        
        for (int i = patternStartIndex;
                 i < patternStartIndex + patternLength; 
                 i++) {
            pattern[i - patternStartIndex] = bits[i];
        }
        
        final boolean bitToPredict = bits[patternStartIndex + patternLength];
        
        if (map.containsKey(pattern)) {
            if (bitToPredict) {
                map.get(pattern).onBits++;
            } else {
                map.get(pattern).offBits++;
            }
        } else {
            final BitFrequencies bitFrequenices = new BitFrequencies();
            
            if (bitToPredict) {
                bitFrequenices.onBits = 1;
            } else {
                bitFrequenices.offBits = 1;
            }
            
            map.put(pattern, bitFrequenices);
        }
    }
}
