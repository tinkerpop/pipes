package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;

/**
 * TransformClosure computes a transformation on the S object, where the PipeClosure determines the E.
 * The first parameter of the PipeClosure is the S of the pipe.
 * The results of the PipeClosure is the E of the pipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TransformClosurePipe<S, E> extends AbstractPipe<S, E> {

    private final PipeClosure<E, Pipe> closure;

    public TransformClosurePipe(final PipeClosure<E, Pipe> closure) {
        this.closure = closure;
        this.closure.setPipe(this);
    }

    public E processNextStart() {
        return this.closure.compute(this.starts.next());
    }
}
