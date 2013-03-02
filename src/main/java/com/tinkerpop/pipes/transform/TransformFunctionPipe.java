package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

/**
 * TransformFunctionPipe computes a transformation on the S object, where the PipeFunction determines the E.
 * The first parameter of the PipeFunction is the S of the pipe.
 * The results of the PipeFunction is the E of the pipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TransformFunctionPipe<S, E> extends AbstractPipe<S, E> implements TransformPipe<S, E> {

    private final PipeFunction<S, E> transformFunction;

    public TransformFunctionPipe(final PipeFunction<S, E> transformFunction) {
        this.transformFunction = transformFunction;
    }

    public E processNextStart() {
        return this.transformFunction.compute(this.starts.next());
    }
}
