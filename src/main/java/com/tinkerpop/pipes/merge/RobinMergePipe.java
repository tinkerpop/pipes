package com.tinkerpop.pipes.merge;

import java.util.Iterator;
import java.util.NoSuchElementException;
import com.tinkerpop.pipes.Pipe;

/**
 * The RobinMergePipe will, in a round robin fashion, process an object from each pipe one in a row until all pipes are exhausted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RobinMergePipe<S> extends AbstractMergePipe<S> {

    private int currentStarts = 0;

    protected S processNextStart() {
        while (true) {
            if (this.allStarts.size() > 0) {
                Iterator<S> starts = this.allStarts.get(this.currentStarts);
                if (starts.hasNext()) {
                    this.currentEnds = starts;
                    this.currentStarts = ++this.currentStarts % this.allStarts.size();
                    return starts.next();
                } else {
                    this.allStarts.remove(this.currentStarts);
                    if (this.allStarts.size() == this.currentStarts)
                        this.currentStarts = 0;
                }
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
