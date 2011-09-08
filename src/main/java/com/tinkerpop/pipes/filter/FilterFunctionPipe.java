package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

/**
 * FilterFunctionPipe is a generic filter pipe.
 * It takes a PipeFunction that returns a Boolean for its compute() step. The argument is the next start of the pipe.
 * If the return is true, then start is emitted. Otherwise, the start is not emitted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FilterFunctionPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final PipeFunction<S, Boolean> function;

    public FilterFunctionPipe(final PipeFunction<S, Boolean> function) {
        this.function = function;
    }

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (function.compute(s))
                return s;
        }
    }
}