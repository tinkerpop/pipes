package com.tinkerpop.pipes.serial.split;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CopySplitPipe<S> extends AbstractSplitPipe<S> {

    public CopySplitPipe(final int numberOfSplits) {
        super(numberOfSplits);
    }

    public void fillNext(final int splitNumber) {
        if (this.hasNext()) {
            S item = this.next();
            for (SplitQueue<S> split : this.splits) {
                split.add(item);
            }
        }
    }
}