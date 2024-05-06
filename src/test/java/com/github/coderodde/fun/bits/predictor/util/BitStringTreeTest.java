package com.github.coderodde.fun.bits.predictor.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public final class BitStringTreeTest {
    
    @Test
    public void addAndGet() {
        final boolean[] data = new boolean[]{ false, true, true };
        BitStringView bitStringView = new BitStringView(data, 0, 3);
        
        final BitStringTree tree = new BitStringTree();
        
        tree.add(bitStringView, true);
        tree.add(bitStringView, true);
        tree.add(bitStringView, true);
        tree.add(bitStringView, false);
        tree.add(bitStringView, false);
        
        BitFrequencyDistribution bf = new BitFrequencyDistribution(5, 0);
        
        assertEquals(tree.get(new BitStringView(new boolean[]{})), bf);
        
        bf = new BitFrequencyDistribution(0, 5);
        
        assertEquals(tree.get(new BitStringView(new boolean[]{ false })), bf);
        assertEquals(tree.get(new BitStringView(new boolean[]{ false,
                                                               true })), bf);
        
        bf = new BitFrequencyDistribution(2, 3);
        
        assertEquals(tree.get(new BitStringView(new boolean[]{ false, 
                                                               true,
                                                               true })), bf);
        
        assertNull(tree.get(new BitStringView(new boolean[]{ true })));
        
        assertNull(tree.get(new BitStringView(new boolean[]{ false, 
                                                             true,
                                                             true,
                                                             false })));
    }
}
