package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Random;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SampleFilterPipe<S> extends AbstractPipe<S, S> {

    private static final Random RANDOM = new Random();
    private final double bias;

    public SampleFilterPipe(double bias) {
        this.bias = bias;
    }

    protected S processNextStart() {
        S s = this.starts.next();
        if (bias >= RANDOM.nextDouble()) {
            return s;
        } else {
            return this.processNextStart();
        }
    }
}
