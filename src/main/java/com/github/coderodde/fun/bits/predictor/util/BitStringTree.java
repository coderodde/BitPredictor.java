package com.github.coderodde.fun.bits.predictor.util;

import com.github.coderodde.fun.bits.predictor.BitFrequencies;
import com.github.coderodde.fun.bits.predictor.BitStringView;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public final class BitStringTree {
    
    private static final class BitStringTreeNode {
        BitStringTreeNode bit0Child;
        BitStringTreeNode bit1Child;
        BitFrequencies bitFrequencies;
        
        BitStringTreeNode() {
            this.bitFrequencies = new BitFrequencies();
        }
    }
    
    private BitStringTreeNode root;
    private int size;
    
    public void add(final BitStringView bitStringView, 
                    final boolean predictedBit) {
        
        if (root == null) {
            root = new BitStringTreeNode();
            size = 1;
        }

        if (predictedBit) {
            root.bitFrequencies.bit[1]++;
        } else {
            root.bitFrequencies.bit[0]++;
        }
        
        BitStringTreeNode current = root;
        
        for (int i = 0; i < bitStringView.length(); i++) {
            if (current == null) {
                return;
            }
            
            final boolean bit = bitStringView.get(i);
            
            if (bit) {
                if (current.bit1Child == null) {
                    current.bit1Child = new BitStringTreeNode();
                    size++;
                }
                
                current.bitFrequencies.bit[1]++;
                current = current.bit1Child;
            } else {
                if (current.bit0Child == null) {
                    current.bit0Child = new BitStringTreeNode();
                    size++;
                }
                
                current.bitFrequencies.bit[0]++;
                current = current.bit0Child;
            }    
        }
    }
    
    public BitFrequencies get(final BitStringView bitStringView) {
        BitStringTreeNode node = root;
        
        for (int i = 0; i < bitStringView.length(); i++) {
            final boolean bit = bitStringView.get(i);
            
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
                            textLine.patternToString(), 
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
                                        textLine.patternToString().length());
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
                             textLine.get0BitFrequencyNumberLength());
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
        
        // Store the current text line:
        textLineList.add(textLine);
        
        // Descend to the left subtree:
        nodePath.addLast(Boolean.FALSE);
        getTextLinesImpl(textLineList, nodePath, root.bit0Child);
        nodePath.removeLast();
        
        // Descend to the right subtree:
        nodePath.addLast(Boolean.TRUE);
        getTextLinesImpl(textLineList, nodePath, root.bit1Child);
        nodePath.removeLast();
    }
    
    private List<List<Boolean>> toList() {
        final List<List<Boolean>> list = new ArrayList<>(size);
        final Deque<Boolean> bitStack = new ArrayDeque<>();
        
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
    
    private static final class TextLine implements Comparable<TextLine> {
        private final int patternValue;
        private final Deque<Boolean> bitPattern;
        private final BitFrequencies bitFrequencies;
        
        TextLine(final Deque<Boolean> bitPattern, 
                 final BitFrequencies bitFrequencies) {
            
            this.patternValue = getPatternValue(bitPattern);
            this.bitPattern = bitPattern;
            this.bitFrequencies = bitFrequencies;
        }
        
        String patternToString() {
            final StringBuilder stringBuilder = new StringBuilder();
            
            for (final Boolean bit : bitPattern) {
                stringBuilder.append((bit ? "1" : "0"));
            }
            
            return stringBuilder.toString();
        }
        
        BitFrequencies getBitFrequencies() {
            return bitFrequencies;
        }
        
        int get1BitFrequencyNumberLength() {
            return String.valueOf(bitFrequencies.bit[1]).length();
        }
        
        int get0BitFrequencyNumberLength() {
            return String.valueOf(bitFrequencies.bit[0]).length();
        }
        
        @Override
        public int compareTo(final TextLine o) {
            return Integer.compare(patternValue, o.patternValue);
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
