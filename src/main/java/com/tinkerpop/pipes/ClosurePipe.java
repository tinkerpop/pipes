package com.tinkerpop.pipes;

/**
 * ClosurePipe is a generic pipe where the pipe's computation is determined by the provided PipeClosure.
 * The method s() is a shortcut method to this.starts.next().
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ClosurePipe<S, E> extends AbstractPipe<S, E> {

    private final PipeClosure<?, ClosurePipe> closure;

    public ClosurePipe(final PipeClosure<?, ClosurePipe> closure) {
        this.closure = closure;
        this.closure.setPipe(this);
    }

    public E processNextStart() {
        return (E) this.closure.compute();
    }

    public S s() {
        return this.starts.next();
    }
}
