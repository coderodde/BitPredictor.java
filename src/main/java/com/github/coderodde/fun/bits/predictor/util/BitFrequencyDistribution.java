package com.github.coderodde.fun.bits.predictor.util;

import java.util.Random;

public final class BitFrequencyDistribution {

    public int[] bit;
    
    public BitFrequencyDistribution() {
        this.bit = new int[2];
    }
    
    public BitFrequencyDistribution(final int bit0, final int bit1) {
        this();
        this.bit[0] = bit0;
        this.bit[1] = bit1;
    }
    
    public void inc0() {
        bit[0]++;
    }
    
    public void inc1() {
        bit[1]++;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("off: ")
                     .append(bit[0])
                     .append(", on: ")
                     .append(bit[1]);

        return stringBuilder.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        final BitFrequencyDistribution other = (BitFrequencyDistribution) o;
        return bit[0] == other.bit[0] && bit[1] == other.bit[1];
    }

    public boolean sample(final Random random) {
        if (bit[0] == 0 && bit[1] == 0) {
            return random.nextBoolean();
        }
        
        return random.nextInt(bit[0] + bit[1]) < bit[1];
    }
}
