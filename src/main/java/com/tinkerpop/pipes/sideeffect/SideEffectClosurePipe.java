package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeClosure;

/**
 * SideEffectClosurePipe will emit the incoming object, but compute the PipeClosure on S.
 * The result of the PipeClosure is not account for.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SideEffectClosurePipe<S> extends AbstractPipe<S, S> {

    private final PipeClosure closure;

    public SideEffectClosurePipe(final PipeClosure closure) {
        this.closure = closure;
        this.closure.setPipe(this);
    }

    public S processNextStart() {
        final S s = this.starts.next();
        this.closure.compute(s);
        return s;
    }
}

