package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

/**
 * SideEffectFunctionPipe will emit the incoming object, but compute the PipeFunction on S.
 * The result of the PipeFunction is not accounted for in the stream.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SideEffectFunctionPipe<S> extends AbstractPipe<S, S> {

    private final PipeFunction<S, ?> sideEffectFunction;

    public SideEffectFunctionPipe(final PipeFunction<S, ?> sideEffectFunction) {
        this.sideEffectFunction = sideEffectFunction;
    }

    public S processNextStart() {
        final S s = this.starts.next();
        this.sideEffectFunction.compute(s);
        return s;
    }
}

