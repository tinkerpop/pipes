package com.tinkerpop.pipes;

/**
 * FunctionPipe is a generic pipe where the pipe's computation is determined by the provided PipeFunction.
 * Note that the PipeFunction.compute() takes the this.starts of the FunctionPipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FunctionPipe<S, E> extends AbstractPipe<S, E> {

    private final PipeFunction function;

    public FunctionPipe(final PipeFunction function) {
        this.function = function;
    }

    public E processNextStart() {
        return (E) this.function.compute(this.starts);
    }
}
