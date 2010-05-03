package com.tinkerpop.pipes.serial.split;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ReadySplitPipe<S> extends AbstractSplitPipe<S> {

    public ReadySplitPipe(final int numberOfSplits) {
        super(numberOfSplits);
    }

    public void fillNext(final int splitNumber) {
        if (this.hasNext()) {
            this.splits.get(splitNumber).add(this.next());
        }
    }
}
