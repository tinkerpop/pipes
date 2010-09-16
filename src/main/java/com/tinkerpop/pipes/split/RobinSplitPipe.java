package com.tinkerpop.pipes.split;

/**
 * The RobinSplitPipe puts each incoming object onto a split in a round robin fashion.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RobinSplitPipe<S> extends AbstractSplitPipe<S> {

    protected int currentSplit = 0;

    public RobinSplitPipe(final int numberOfSplits) {
        super(numberOfSplits);
    }

    public void routeNext() {
        final int splitSize = this.splits.size();
        for (int i = 0; i < splitSize; i++) {
            if (this.hasNext()) {
                SplitQueuePipe<S> split = this.splits.get(this.currentSplit);
                split.add(this.next());
                if (pathEnabled) {
                    split.addPath(this.path());
                }
                this.currentSplit = ++this.currentSplit % splitSize;
            } else {
                break;
            }
        }
    }
}
