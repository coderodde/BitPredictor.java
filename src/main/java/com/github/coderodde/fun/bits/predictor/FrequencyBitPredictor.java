package com.github.coderodde.fun.bits.predictor;

import java.util.Objects;
import java.util.Random;

public final class FrequencyBitPredictor implements BitPredictor {

    private final Random random;
    private final boolean[] learningBitString;
    private final BitFrequencies bitFrequencies = new BitFrequencies();
    
    public FrequencyBitPredictor(final boolean[] learningBitString,
                                 final Random random) {
        this.learningBitString =
                Objects.requireNonNull(
                        learningBitString, 
                        "The input learning bit string is null.");
        
        this.random = 
                Objects.requireNonNull(random, "The input Random is null.");
        
        process();
    }
    
    @Override
    public boolean predict(boolean[] bitString) {
        return bitFrequencies.sample(random);
    }
    
    @Override
    public boolean[] predictArray() {
        final boolean[] bitString = new boolean[learningBitString.length];
        
        for (int i = 0; i < bitString.length; i++) {
            bitString[i] = bitFrequencies.sample(random);
        }
        
        return bitString;
    }
    
    private void process() {
        for (final boolean bit : learningBitString) {
            if (bit) {
                bitFrequencies.onBits++;
            } else {
                bitFrequencies.offBits++;
            }
        }
    }
}
