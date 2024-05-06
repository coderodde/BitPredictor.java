package com.github.coderodde.fun.bits.predictor.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public final class BitStringTree {
    
    private static final class BitStringTreeNode {
        BitStringTreeNode bit0Child;
        BitStringTreeNode bit1Child;
        BitFrequencyDistribution bitFrequencies;
        
        BitStringTreeNode() {
            this.bitFrequencies = new BitFrequencyDistribution();
        }
    }
    
    private final BitStringTreeNode root = new BitStringTreeNode();
    private int size = 1; // Count the root.
     
    public void add(final BitStringView bitStringView, 
                    final boolean predictedBit) {
        
        BitStringTreeNode current = root;
        
        for (int i = 0; i < bitStringView.length(); i++) {
            final boolean bit = bitStringView.get(i);
            
            if (bit) {
                if (current.bit1Child == null) {
                    current.bit1Child = new BitStringTreeNode();
                    size++;
                }
                
                current.bitFrequencies.incrementOneBitCounter();
                current = current.bit1Child;
            } else {
                if (current.bit0Child == null) {
                    current.bit0Child = new BitStringTreeNode();
                    size++;
                }
                
                current.bitFrequencies.incrementZeroBitCounter();
                current = current.bit0Child;
            }    
        }
        
        if (predictedBit) {
            current.bitFrequencies.incrementOneBitCounter();
        } else {
            current.bitFrequencies.incrementZeroBitCounter();
        }
    }
    
    public BitFrequencyDistribution get(final BitStringView bitStringView) {
        BitStringTreeNode node = root;
        
        for (int i = 0; i < bitStringView.length(); i++) {
            final boolean bit = bitStringView.get(i);
            
            if (bit) {
                if (node.bit1Child == null) {
                    return null;
                }
                
                node = node.bit1Child;
            } else {
                if (node.bit0Child == null) {
                    return null;
                }
                
                node = node.bit0Child;
            }
        }
        
        return node.bitFrequencies;
    }
    
    public int size() {
        return size;
    }
    
    @Override
    public String toString() {
        final List<TextLine> textLineList = getTextLines();
        
        final int maximumPatternStringLength = 
                getMaximumPatternStringLength(textLineList);
        
        final int maximum0bitCountStringLength = 
                getMaximum0bitCountStringLength(textLineList);
        
        final int maximum1bitCountStringLength = 
                getMaximum1bitCountStringLength(textLineList);
        
        final int maximumLineNumberStringLength =
                Integer.toString(size).length();
        
        final String formatString = 
                "Pattern %%%dd: \"%%%ds\", ones = %%%dd, zeros = %%%dd\n";
        
        final String formatString2 =
                String.format(
                        formatString,
                        maximumLineNumberStringLength,
                        maximumPatternStringLength,
                        maximum1bitCountStringLength, 
                        maximum0bitCountStringLength);
        
        Collections.sort(textLineList);
        
        final StringBuilder stringBuilder = new StringBuilder();
        
        int lineNumber = 1;
        
        for (final TextLine textLine : textLineList) {
            stringBuilder.append(
                    String.format(
                            formatString2, 
                            lineNumber++, 
                            textLine.patternString,
                            textLine.bitFrequencies.bit[1],
                            textLine.bitFrequencies.bit[0]));
        }
        
        // Delete the last "\n" string:
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
    
    private int getMaximumPatternStringLength(
            final List<TextLine> textLineList) {
        
        int tentativeMaximum = 0;
        
        for (final TextLine textLine : textLineList) {
            tentativeMaximum = Math.max(tentativeMaximum, 
                                        textLine.patternString.length());
        }
        
        return tentativeMaximum;
    }
    
    private int getMaximum0bitCountStringLength(
            final List<TextLine> textLineList) {
        
        int tentativeMaximum = 0;
        
        for (final TextLine textLine : textLineList) {
            tentativeMaximum = 
                    Math.max(tentativeMaximum, 
                             textLine.get0BitFrequencyNumberLength());
        }
        
        return tentativeMaximum;
    }
    
    private int getMaximum1bitCountStringLength(
            final List<TextLine> textLineList) {
        
        int tentativeMaximum = 0;
        
        for (final TextLine textLine : textLineList) {
            tentativeMaximum = 
                    Math.max(tentativeMaximum, 
                             textLine.get1BitFrequencyNumberLength());
        }
        
        return tentativeMaximum;
    }
    
    private List<TextLine> getTextLines() {
        List<TextLine> textLineList = new ArrayList<>(size);
        
        getTextLinesImpl(textLineList,
                         new ArrayDeque<>(),
                         root);
        
        return textLineList;
    }
    
    private void getTextLinesImpl(final List<TextLine> textLineList, 
                                  final Deque<Boolean> nodePath,
                                  final BitStringTreeNode root) {
        if (root == null) {
            // End the recursion.
            return;
        }
        
        // Construct current text line:
        final TextLine textLine = new TextLine(nodePath,
                                               root.bitFrequencies);
        
        // Descend to the left subtree:
        nodePath.addLast(Boolean.FALSE);
        getTextLinesImpl(textLineList, nodePath, root.bit0Child);
        nodePath.removeLast();
        
        // Store the current text line:
        textLineList.add(textLine);
        
        // Descend to the right subtree:
        nodePath.addLast(Boolean.TRUE);
        getTextLinesImpl(textLineList, nodePath, root.bit1Child);
        nodePath.removeLast();
    }
    
    private static final class TextLine implements Comparable<TextLine> {
        private final int patternNumber;
        private final String patternString;
        private final BitFrequencyDistribution bitFrequencies;
        
        TextLine(final Deque<Boolean> bitPattern, 
                 final BitFrequencyDistribution bitFrequencies) {
            
            this.patternNumber  = getPatternValue(bitPattern);
            this.patternString  = convertPatternToString(bitPattern);
            this.bitFrequencies = bitFrequencies;
        }
        
        @Override
        public String toString() {
            final StringBuilder stringBuilder = new StringBuilder();
            
            stringBuilder.append("[TextLine | pattern value = ")
                         .append(patternNumber)
                         .append(" | bit pattern = ")
                         .append(patternString)
                         .append(" | bit freqs = ")
                         .append(bitFrequencies)
                         .append("]");
            
            return stringBuilder.toString();
        }
        
        String convertPatternToString(final Deque<Boolean> bitPattern) {
            final StringBuilder stringBuilder = new StringBuilder();
            
            for (final Boolean bit : bitPattern) {
                stringBuilder.append((bit ? "1" : "0"));
            }
            
            return stringBuilder.toString();
        }
        
        int get1BitFrequencyNumberLength() {
            return String.valueOf(bitFrequencies.bit[1]).length();
        }
        
        int get0BitFrequencyNumberLength() {
            return String.valueOf(bitFrequencies.bit[0]).length();
        }
        
        @Override
        public int compareTo(final TextLine o) {
            return patternString.compareTo(o.patternString);
        }
        
        private static int getPatternValue(final Deque<Boolean> bitPattern) {
            int value = 0;
            
            for (final boolean bit : bitPattern) {
                value <<= 1;
                
                if (bit) {
                    value |= 0b1;
                }
            }
            
            return  value;
        }
    }
}
