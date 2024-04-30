package com.github.coderodde.fun.bits.predictor.util;

import com.github.coderodde.fun.bits.predictor.BitFrequencies;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class BitStringTree {
    
    private static final class BitStringTreeNode {
        BitStringTreeNode bit0Child;
        BitStringTreeNode bit1Child;
        BitFrequencies bitFrequencies;
    }
    
    private BitStringTreeNode root;
    private int size;
    
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
            size++;
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
    
    public int size() {
        return size;
    }
    
    public List<boolean[]> toList() {
        final List<boolean[]> list = new ArrayList<>(size);
        final Deque<Boolean> bitStack = new ArrayDeque<>();
        
        toListImpl(list, bitStack, root);
        
        return list;
    }
    
    private static boolean[] toBooleanArray(final Deque<Boolean> bitStack) {
        final boolean[] bitArray = new boolean[bitStack.size()];
        int i = 0;
        
        for (final Boolean bit : bitStack) {
            bitArray[i++] = bit;
        }
        
        return bitArray;
    }
    
    public void toListImpl(final List<boolean[]> list, 
                           final Deque<Boolean> bitStack,
                           final BitStringTreeNode node) {
        if (node == null) {
            return;
        }
        
        if (node.bitFrequencies != null) {
            list.addAll(toBooleanArray(bitStack));
        }
        
        if (node.bit0Child != null) {
            list
        }
    }
}
