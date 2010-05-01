package com.tinkerpop.pipes.serial.split;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SingleSplitPipe<S> extends AbstractSplitPipe<S> {

    public SingleSplitPipe(final int numberOfSplits) {
        super(numberOfSplits);
    }

    public void fillNext(int splitNumber) {
        if (this.hasNext()) {
            this.splits.get(splitNumber).add(this.next());
        }
    }
}
