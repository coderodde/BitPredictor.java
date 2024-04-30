package com.github.coderodde.fun.bits.predictor;

import java.util.Random;

public final class BitFrequencies {

    public int[] bit;
    
    public BitFrequencies() {
        this.bit = new int[2];
    }

    void add(final BitFrequencies otherBitDistribution) {
        this.bit[0] += otherBitDistribution.bit[0];
        this.bit[1] += otherBitDistribution.bit[1];
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

    boolean sample(final Random random) {
        return random.nextInt(bit[0] + bit[1]) < bit[0];
    }
}
