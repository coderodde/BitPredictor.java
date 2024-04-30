package com.github.coderodde.fun.bits.predictor.util;

import com.github.coderodde.fun.bits.predictor.BitFrequencies;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import org.junit.Test;

public final class BitStringTreeTest {
    
    @Test
    public void addAndGet() {
        final BitStringTree tree = new BitStringTree();
        
        tree.add(new boolean[]{ false, true, true }, true);
        tree.add(new boolean[]{ false, true, true }, true);
        tree.add(new boolean[]{ false, true, true }, false);
        
        assertNull(tree.get(new boolean[]{ true, true }));
        assertNull(tree.get(new boolean[]{ false, true }));
        assertNull(tree.get(new boolean[]{ true, false }));
        assertNull(tree.get(new boolean[]{ false, false }));
        
        BitFrequencies f = tree.get(new boolean[]{ false, true, true});
        
        assertEquals(1, f.bit[0]);
        assertEquals(2, f.bit[1]);
    }
}
