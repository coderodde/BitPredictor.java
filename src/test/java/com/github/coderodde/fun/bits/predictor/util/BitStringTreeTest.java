package com.github.coderodde.fun.bits.predictor.util;

import com.github.coderodde.fun.bits.predictor.BitFrequencies;
import com.github.coderodde.fun.bits.predictor.BitStringView;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
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
        
        BitFrequencies bitFrequencies = tree.get(bitStringView);
        
        System.out.println(bitFrequencies);
    }
}
