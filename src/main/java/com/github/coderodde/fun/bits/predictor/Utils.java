package com.github.coderodde.fun.bits.predictor;

import java.util.Objects;

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
}
