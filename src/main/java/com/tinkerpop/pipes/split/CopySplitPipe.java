package com.tinkerpop.pipes.split;

import java.util.Iterator;
import java.util.ArrayList;

/**
 * The CopySplitPipe adds each incoming object to each of the outgoing splits.
 * Thus, each split has the same number of objects outgoing from it.
 * This is also the same number of objects that are incoming to the CopySplitPipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CopySplitPipe<S> extends AbstractSplitPipe<S> {

    public CopySplitPipe(final int numberOfSplits) {
        super(numberOfSplits);
    }

    public void routeNext() {
        if (this.hasNext()) {
            S item = this.next();
            ArrayList splitPath = null;
            if (pathEnabled) {
                Iterator<SplitQueuePipe<S>> iter = this.splits.iterator();
                SplitQueuePipe<S> split = iter.next();
                splitPath = split.splitPath();
            }
            for (SplitQueuePipe<S> split : this.splits) {
                split.add(item);
                if (pathEnabled) {
                    split.addPath(splitPath);
                }
            }
        }
    }
}
