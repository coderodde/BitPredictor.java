package com.github.coderodde.fun.bits.predictor;

import java.util.Random;

public final class BitFrequencies {

    public int[] bit;
    
    public BitFrequencies() {
        this.bit = new int[2];
    }
    
    public BitFrequencies(final int bit0, final int bit1) {
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
        final BitFrequencies other = (BitFrequencies) o;
        return bit[0] == other.bit[0] && bit[1] == other.bit[1];
    }

    boolean sample(final Random random) {
        return random.nextInt(bit[0] + bit[1]) < bit[0];
    }
}
