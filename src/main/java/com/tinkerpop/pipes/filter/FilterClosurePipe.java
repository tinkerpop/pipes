package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;

/**
 * FilterClosurePipe is a generic filter pipe.
 * It takes a PipeClosure that returns an Object for in its compute() step when the argument is the start of the pipe.
 * If the return is true, then start is emitted. Otherwise, the start is not emitted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FilterClosurePipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final PipeClosure<Boolean, Pipe> closure;

    public FilterClosurePipe(final PipeClosure<Boolean, Pipe> closure) {
        this.closure = closure;
        this.closure.setPipe(this);
    }

    public S processNextStart() {
        while (true) {
            final S start = this.starts.next();
            if (closure.compute(start))
                return start;
        }
    }
}