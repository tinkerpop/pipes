package com.tinkerpop.pipes;

/**
 * ClosurePipe is a generic pipe where the pipe's computation is determined by the provided PipeFunction.
 * Note that the PipeFunction.compute() takes the this.starts of the ClosurePipe.
 * The method s() is a shortcut method to this.starts.next().
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ClosurePipe<S, E> extends AbstractPipe<S, E> {

    private final PipeFunction function;

    public ClosurePipe(final PipeFunction function) {
        this.function = function;
    }

    public E processNextStart() {
        return (E) this.function.compute(this.starts);
    }

    /**
     * A shorthand method that is equivalent to this.starts.next()
     *
     * @return the this.starts.next()
     */
    public S s() {
        return this.starts.next();
    }
}
