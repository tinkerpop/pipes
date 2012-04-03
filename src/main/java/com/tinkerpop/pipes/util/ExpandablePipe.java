package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExpandablePipe<S> extends AbstractPipe<S, S> {

    protected Queue<S> queue = new LinkedList<S>();

    public S processNextStart() {
        if (!this.queue.isEmpty())
            return this.queue.remove();
        else
            return this.starts.next();
    }

    public void add(S s) {
        this.queue.add(s);
    }

    public void reset() {
        this.queue.clear();
        super.reset();
    }
}
