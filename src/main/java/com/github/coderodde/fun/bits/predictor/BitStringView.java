package com.github.coderodde.fun.bits.predictor;

public class BitStringView {
    private final boolean[] bits;
    private final int startIndex;
    private final int length;
    
    public BitStringView(final boolean[] bits,
                         final int startIndex,
                         final int length) {
        this.bits = bits;
        this.startIndex = startIndex;
        this.length = length;
    }
    
    public int length() {
        return length;
    }
    
    public boolean get(final int index) {
        return bits[startIndex + index];
    }
    
    public int getStartIndex() {
        return startIndex;
    }
}
