package com.github.coderodde.fun.bits.predictor;

import static com.github.coderodde.fun.bits.predictor.Utils.checkIsPositiveValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * This class implements a 
 */
public final class MachineLearningBitPredictor implements BitPredictor {
    
    private static final int MAXIMUM_PATTERN_LENGTH_UPPER_BOUND = 26;
    private static final int DEFAULT_PATTERN_LENGTH_UPPER_BOUND = 10;

    private final boolean[] bits;
    private final int maximumPatternLength;
    private final Random random;
    private final Map<BitString, BitFrequencies> map = new HashMap<>();
    
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
        
        for (final Map.Entry<BitString, BitFrequencies> e : map.entrySet()) {
            final boolean[] pattern = e.getKey().getBitArray();
            
            if (pattern.length > bitString.length) {
                // Once here, the current pattern is longer than the input bit
                // string; omit it.
                continue;
            }
            
            final BitString patternSuffix = 
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
    public boolean[] predictArray(final int length) {
        checkIsPositiveValue(
                length,
                String.format(
                        "The length is too small (%d). Must be at least 1.",
                        length));
        
        final boolean[] predictedArray = new boolean[bits.length];
        
        predictedArray[0] = bits[0];
        
        return predictedArray;
    }
    
    private static BitString getPatternSuffix(final boolean[] pattern, 
                                              final int suffixLength) {
        
        final boolean[] patternSuffix = new boolean[suffixLength];
        
        for (int i = pattern.length - suffixLength, j = 0; 
                 j < suffixLength; 
                 j++, i++) {
            
            patternSuffix[j] = pattern[i];
        }
        
        return new BitString(patternSuffix);
    }
    
    @Override
    public String toString() {
        final int maximum1BitCountLength = getMaximum1BitCountLength();
        final int maximum0BitCountlength = getMaximum0BitCountLength();
        
        final String fmt = 
                String.format(
                        "bits = %%%ds, 0s = %%%dd, 1s = %%%dd.",
                        maximumPatternLength,
                        maximum0BitCountlength,
                        maximum1BitCountLength);
        
        final List<String> stringList = new ArrayList<>(map.size());
        
        for (final Map.Entry<BitString, BitFrequencies> e : map.entrySet()) {
            stringList.add(
                    String.format(
                            fmt,
                            e.getKey(), 
                            e.getValue().bit[0],
                            e.getValue().bit[1]));
        }
        
        stringList.sort(String::compareTo);
        
        final StringBuilder stringBuilder = new StringBuilder();
        final int numberOfLines = map.size();
        final int lineNumberStringMaximumLength = 
                Integer.toString(numberOfLines).length();
        final String fmt2 = String.format("Line %%%dd: %%s.\n",
                                          lineNumberStringMaximumLength);
        
        int lineNumber = 1;
        
        for (final String string : stringList) {
            stringBuilder.append(
                    String.format(
                            fmt2, 
                            lineNumber++, 
                            string));
        }
        
        // Delete the concluding "\n":
        stringBuilder.deleteCharAt(stringBuilder.length() - 1); 
        return stringBuilder.toString();
    }
    
    private int getMaximum1BitCountLength() {
        int maximumValue = 0;
        
        for (final BitFrequencies bitFrequencies : map.values()) {
            maximumValue = Math.max(maximumValue, bitFrequencies.bit[1]);
        }
        
        return String.valueOf(maximumValue).length();
    }
    
    private int getMaximum0BitCountLength() {
        int maximumValue = 0;
        
        for (final BitFrequencies bitFrequencies : map.values()) {
            maximumValue = Math.max(maximumValue, bitFrequencies.bit[0]);
        }
        
        return String.valueOf(maximumValue).length();
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
        final BitString patternBitString = new BitString(pattern);
        
        if (map.containsKey(patternBitString)) {
            if (bitToPredict) {
                map.get(patternBitString).bit[1]++;
            } else {
                map.get(patternBitString).bit[0]++;
            }
        } else {
            final BitFrequencies bitFrequenices = new BitFrequencies();
            
            if (bitToPredict) {
                bitFrequenices.bit[1] = 1;
            } else {
                bitFrequenices.bit[0] = 1;
            }
            
            map.put(patternBitString, bitFrequenices);
        }
    }
}
