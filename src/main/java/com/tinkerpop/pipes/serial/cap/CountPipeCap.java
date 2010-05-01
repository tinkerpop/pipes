package com.tinkerpop.pipes.serial.cap;

import com.tinkerpop.pipes.serial.AbstractPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CountPipeCap<S> extends AbstractPipe<S, Long> {

    protected Long processNextStart() {
        long counter = 0l;
        while (this.starts.hasNext()) {
            counter++;
            this.starts.next();
        }
        return counter;
    }
}
