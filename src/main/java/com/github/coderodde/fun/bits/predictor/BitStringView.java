package com.github.coderodde.fun.bits.predictor;

import java.util.Arrays;

public class BitStringView {
    private final boolean[] bits;
    private final int startIndex;
    private int length;
    
    public BitStringView(final boolean[] bits,
                         final int startIndex,
                         final int length) {
        this.bits = bits;
        this.startIndex = startIndex;
        this.length = length;
    }
    
    public BitStringView(final boolean[] bits) {
        this(bits, 0, bits.length);
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
    
    public void contractFromTail(final int bitStringLength) {
        if (startIndex + length > bitStringLength) {
            length -= startIndex + length - bitStringLength;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            stringBuilder.append((get(i) ? "1" : "0"));
        }
        
        return stringBuilder.toString();
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        
        for (int i = 0; i < length; i++) {
            final boolean element = get(i);
            result = 31 * result + (element ? 1231 : 1237);
        }
        
        return result;
    }
    
    @Override
    public boolean equals(final Object o) {
        final BitStringView other = (BitStringView) o;
        
        if (length != other.length()) {
            return false;
        }
        
        for (int i = 0; i < length; i++) {
            if (get(i) != other.get(i)) {
                return false;
            }
        }
        
        return true;
    }
}
