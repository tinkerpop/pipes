package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractSplitPipe<S> extends AbstractPipe<S, S> implements SplitPipe<S> {

    protected final List<SplitQueue<S>> splits = new ArrayList<SplitQueue<S>>();

    public AbstractSplitPipe(int numberOfSplits) {
        for (int i = 0; i < numberOfSplits; i++) {
            this.addSplit();
        }
    }

    public void addSplit() {
        this.splits.add(new SplitQueue<S>(this, this.splits.size()));
    }

    public Iterator<S> getSplit(final int number) {
        return this.splits.get(number);
    }

    public void setStarts(final Iterator<S> starts) {
        super.setStarts(starts);
    }

    protected S processNextStart() {
        return this.starts.next();
    }
}
