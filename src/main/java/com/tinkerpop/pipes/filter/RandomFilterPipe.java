package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Random;

/**
 * The RandomFilterPipe filters out objects that pass through it using a biased coin.
 * For each passing object, a random number generator creates a double value between 0 and 1.
 * If the randomly generated double is less than or equal the provided bias, then the object is allowed to pass.
 * If the randomly generated double is greater than the provided bias, then the object is not allowed to pass.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RandomFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private static final Random RANDOM = new Random();
    private final double bias;

    public RandomFilterPipe(final double bias) {
        this.bias = bias;
    }

    protected S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (this.bias >= RANDOM.nextDouble()) {
                return s;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.bias);
    }
}
