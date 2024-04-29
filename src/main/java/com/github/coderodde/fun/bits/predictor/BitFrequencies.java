package com.github.coderodde.fun.bits.predictor;

import java.util.Random;

final class BitFrequencies {

    int offBits;
    int onBits;

    void add(final BitFrequencies otherBitDistribution) {
        this.offBits += otherBitDistribution.offBits;
        this.onBits += otherBitDistribution.onBits;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("off: ")
                .append(offBits)
                .append(", on: ")
                .append(onBits);

        return stringBuilder.toString();
    }

    boolean sample(final Random random) {
        return random.nextInt(onBits + offBits) < offBits;
    }
}
