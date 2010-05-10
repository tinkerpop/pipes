package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractSplitPipe<S> extends AbstractPipe<S, S> implements SplitPipe<S> {

    protected final List<SplitQueuePipe<S>> splits = new ArrayList<SplitQueuePipe<S>>();

    public AbstractSplitPipe(int numberOfSplits) {
        for (int i = 0; i < numberOfSplits; i++) {
            this.addSplit();
        }
    }

    public void addSplit() {
        this.splits.add(new SplitQueuePipe<S>(this));
    }

    public SplitQueuePipe<S> getSplit(final int splitNumber) {
        return this.splits.get(splitNumber);
    }

    protected S processNextStart() throws NoSuchElementException {
        return this.starts.next();
    }
}
