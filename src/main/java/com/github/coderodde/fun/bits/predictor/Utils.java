package com.github.coderodde.fun.bits.predictor;

public final class Utils {
    
    public static void checkIsPositiveValue(final int value, 
                                            final String exceptionMessage) {
        if (value < 1) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
