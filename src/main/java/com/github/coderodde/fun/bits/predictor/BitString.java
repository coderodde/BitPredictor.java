package com.github.coderodde.fun.bits.predictor;

import java.util.Arrays;
import java.util.Objects;

final class BitString {
    private final boolean[] bits;
    
    BitString(final boolean[] bits) {
        this.bits = Objects.requireNonNull(bits, "The input bits are null.");
    }
    
    boolean[] getBitArray() {
        return bits;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final BitString other = (BitString) obj;
        
        return Arrays.equals(this.bits, other.bits);
    }
    
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        
        for (final boolean bit : bits) {
            stringBuilder.append((bit ? "1" : "0"));
        }
        
        return stringBuilder.toString();
    }
}
