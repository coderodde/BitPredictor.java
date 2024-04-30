package com.github.coderodde.fun.bits.predictor.util;

import com.github.coderodde.fun.bits.predictor.BitFrequencies;

public final class BitStringTree {
    
    private static final class BitStringTreeNode {
        BitStringTreeNode bit0Child;
        BitStringTreeNode bit1Child;
        BitFrequencies bitFrequencies;
    }
    
    private BitStringTreeNode root;
    
    public void add(final boolean[] bitString, final boolean predictedBit) {
        if (root == null) {
            root = new BitStringTreeNode();
        }
        
        BitStringTreeNode current = root;
        
        for (int i = 0; i < bitString.length; i++) {
            final boolean bit = bitString[i];
            
            if (bit) {
                if (current.bit1Child == null) {
                    current.bit1Child = new BitStringTreeNode();
                }
                
                current = current.bit1Child;
            } else {
                if (current.bit0Child == null) {
                    current.bit0Child = new BitStringTreeNode();
                } 
                
                current = current.bit0Child;
            }
        }
        
        BitFrequencies bitFrequencies = current.bitFrequencies;
        
        if (bitFrequencies == null) {
            current.bitFrequencies = bitFrequencies = new BitFrequencies();
        }
        
        if (predictedBit) {
            bitFrequencies.bit[1]++;
        } else {
            bitFrequencies.bit[0]++;
        }
    }
    
    public BitFrequencies get(final boolean[] bitString) {
        BitStringTreeNode node = root;
        
        for (int i = 0; i < bitString.length; i++) {
            final boolean bit = bitString[i];
            
            if (bit) {
                if (node.bit1Child == null) {
                    return node.bitFrequencies;
                }
                
                node = node.bit1Child;
            } else {
                if (node.bit0Child == null) {
                    return node.bitFrequencies;
                }
                
                node = node.bit0Child;
            }
        }
        
        return node.bitFrequencies;
    }
}
