package com.github.coderodde.fun.bits.predictor;

/**
 * This interface define the API for predicting bit strings.
 */
public interface BitPredictor {
    
    /**
     * Predicts a single bit that could follow (possibly) the {@code bitString}.
     * 
     * @param bitString the bit string to predict.
     * 
     * @return the predicted bit. {@code true} for 1, and {@code false} for 0.
     */
    public boolean predict(final boolean[] bitString);
    
    /**
     * Predicts an array of the same length as the internal learning bit string.
     * 
     * @return the predicted bit string.
     */
    public boolean[] predictArray();
}
