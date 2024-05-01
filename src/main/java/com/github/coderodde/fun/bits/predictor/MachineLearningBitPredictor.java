package com.github.coderodde.fun.bits.predictor;

import static com.github.coderodde.fun.bits.predictor.Utils.checkIsPositiveValue;
import com.github.coderodde.fun.bits.predictor.util.BitStringTree;
import java.util.Objects;
import java.util.Random;

/**
 * This class implements a machine learning approach to predicting the bit 
 * strings.
 */
public final class MachineLearningBitPredictor implements BitPredictor {
    
    private static final int MAXIMUM_PATTERN_LENGTH_UPPER_BOUND = 26;
    private static final int DEFAULT_PATTERN_LENGTH_UPPER_BOUND = 10;

    private final boolean[] bits;
    private final int maximumPatternLength;
    private final Random random;
    private final BitStringTree bitStringTree = new BitStringTree();

    public MachineLearningBitPredictor(final boolean[] bits, 
                                       final int maximumPatternLength,
                                       final Random random) {
        
        this.bits = Objects.requireNonNull(bits, "The input bits is null.");
        this.maximumPatternLength = 
                checkMaximumPatternLength(maximumPatternLength);
        
        this.random = Objects.requireNonNull(random,
                                             "The input Random is null.");
        learnModel();
    }
    
    public MachineLearningBitPredictor(final boolean[] bits, 
                                       final Random random) {
        this(bits, DEFAULT_PATTERN_LENGTH_UPPER_BOUND, random);
    }
    
    @Override
    public boolean predict(final BitStringView bitStringView) {
        // Start from the largest pattern lengths:
        for (int patternLength = maximumPatternLength; 
                 patternLength >= 0; 
                 patternLength--) {
            
            if (patternLength > bitStringView.length()) {
                // Once here, the current pattern is longer than the input bit
                // string view, omit:
                continue;
            }
            
            // Get the patternLength long pattern suffix:
            final BitStringView patternSuffixView = 
                    getPatternSuffix(bitStringView, patternLength);
            
            patternSuffixView.contractFromTail(bits.length);
            
            // Grab the bit frequencies:
            final BitFrequencies bitFrequencies = 
                    bitStringTree.get(patternSuffixView);
            
            // Sample the bit:
            return bitFrequencies.sample(random);
        }
        
        throw new IllegalStateException("Should not get here.");
    }
    
    @Override
    public boolean[] predictArray(final int length) {
        checkIsPositiveValue(
                length,
                String.format(
                        "The length is too small (%d). Must be at least 1.",
                        length));
        
        final boolean[] predictedArray = new boolean[bits.length];
        
        for (int i = 0; i < length; i++) {
            final BitStringView bitStringView =
                    new BitStringView(
                            bits,
                            i,
                            maximumPatternLength);
            
            predictedArray[i] = predict(bitStringView);
        }
        
        return predictedArray;
    }
    
    private BitStringView getPatternSuffix(final BitStringView pattern, 
                                           final int suffixLength) {
        
        return new BitStringView(
                bits, 
                pattern.getStartIndex() + pattern.length() - suffixLength, 
                suffixLength);
    }
    
    @Override
    public String toString() {
        return bitStringTree.toString();
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
    
    private void learnModel() {
        for (int patternLength = 0; 
                 patternLength <= maximumPatternLength; 
                 patternLength++) {
            
            learnModelForPatternLength(patternLength);
        }
    }
    
    private void learnModelForPatternLength(final int patternLength) {
        for (int patternStartIndex = 0; 
                 patternStartIndex < bits.length - patternLength; 
                 patternStartIndex++) {
            
            loadPattern(patternStartIndex, 
                        patternLength);
        }
    }
    
    private void loadPattern(final int patternStartIndex, 
                             final int patternLength) {
        
        final BitStringView pattern = 
                new BitStringView(
                        bits, 
                        patternStartIndex, 
                        patternLength);
        
        final boolean bitToPredict = bits[patternStartIndex + patternLength];
        final BitFrequencies bitFrequencies = bitStringTree.get(pattern);
        
        if (bitFrequencies != null) {
            if (bitToPredict) {
                bitFrequencies.bit[1]++;
            } else {
                bitFrequencies.bit[0]++;
            }
        } else {
            bitStringTree.add(pattern, bitToPredict);
        }
    }
}
