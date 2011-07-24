package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeClosure;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FilterClosurePipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final PipeClosure closure;

    public FilterClosurePipe(final PipeClosure closure) {
        this.closure = closure;
        this.closure.setPipe(this);
    }

    public S processNextStart() {
        while (true) {
            final S start = this.starts.next();
            final Object result = closure.compute(start);
            if (result instanceof Boolean) {
                if ((Boolean) result)
                    return start;
            } else if (null != result)
                return start;
        }
    }
}