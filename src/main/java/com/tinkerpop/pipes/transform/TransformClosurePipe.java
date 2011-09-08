package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

/**
 * TransformClosure computes a transformation on the S object, where the PipeFunction determines the E.
 * The first parameter of the PipeFunction is the S of the pipe.
 * The results of the PipeFunction is the E of the pipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TransformClosurePipe<S, E> extends AbstractPipe<S, E> {

    private final PipeFunction<S, E> function;

    public TransformClosurePipe(final PipeFunction<S, E> function) {
        this.function = function;
    }

    public E processNextStart() {
        return this.function.compute(this.starts.next());
    }
}
