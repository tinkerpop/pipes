package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Iterator;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractSplitPipe<S> extends AbstractPipe<S, S> implements SplitPipe<S> {

    protected SplitQueue<S>[] splits;
    protected final int numberOfSplits;

    public AbstractSplitPipe(int numberOfSplits) {
        this.numberOfSplits = numberOfSplits;
        this.splits = new SplitQueue[numberOfSplits];
        for (int i = 0; i < numberOfSplits; i++) {
            this.splits[i] = new SplitQueue<S>(this, i);
        }
    }

    public Iterator<S> getSplit(final int number) {
        return this.splits[number];
    }

    public void setStarts(final Iterator<S> starts) {
        super.setStarts(starts);
    }

    protected S processNextStart() {
        return this.starts.next();
    }
}
