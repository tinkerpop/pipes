package com.tinkerpop.pipes.split.demo;

import com.tinkerpop.pipes.split.AbstractSplitPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ThreeBinSortSplitPipe extends AbstractSplitPipe<Integer> {

    public ThreeBinSortSplitPipe() {
        super(3);
    }

    public void routeNext() {
        if (this.hasNext()) {
            Integer number = this.next();
            if (number < 4) {
                this.splits.get(0).add(number);
            } else if (number >= 4 && number < 7) {
                this.splits.get(1).add(number);
            } else {
                this.splits.get(2).add(number);
            }
        }
    }
}