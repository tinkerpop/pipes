package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractSplitPipe<S> extends AbstractPipe<S, S> implements SplitPipe<S> {

    protected final List<SplitQueue<S>> splits;
    protected int numberOfSplits;

    public AbstractSplitPipe(int numberOfSplits) {

        // todo: use add splot?
        this.numberOfSplits = numberOfSplits;
        this.splits = new ArrayList<SplitQueue<S>>(this.numberOfSplits);
        for (int i = 0; i < numberOfSplits; i++) {
            splits.add(new SplitQueue<S>(this, i));
        }
    }

    public void addSplit() {
        this.splits.add(new SplitQueue<S>(this, ++this.numberOfSplits));
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
