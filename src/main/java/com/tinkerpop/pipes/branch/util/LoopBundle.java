package com.tinkerpop.pipes.branch.util;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopBundle<T> {
    private final T t;
    private final int step;

    public LoopBundle(final T t, final int step) {
        this.t = t;
        this.step = step;
    }

    public int getStep() {
        return this.step;
    }

    public T getObject() {
        return this.t;
    }
}