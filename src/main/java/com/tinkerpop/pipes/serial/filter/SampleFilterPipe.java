package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * The SampleFilterPipe filters out objects that pass through it using a biased coin.
 * For each passing object, a random number generator creates a double value between 0 and 1.
 * If the randomly generated double is less than or equal the provided bias, then the object is allowed to pass.
 * If the randomly generated double is greater than the provided bias, then the object is not allowed to pass.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SampleFilterPipe<S> extends AbstractPipe<S, S> {

    private static final Random RANDOM = new Random();
    private final double bias;

    public SampleFilterPipe(final double bias) {
        this.bias = bias;
    }

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            if (bias >= RANDOM.nextDouble()) {
                return s;
            }
        }
        throw new NoSuchElementException();
    }
}
