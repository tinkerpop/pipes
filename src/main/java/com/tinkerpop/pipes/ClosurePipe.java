package com.tinkerpop.pipes;

/**
 * ClosurePipe is a generic pipe where the pipe's computation is determined by the provided PipeClosure.
 * Note that the PipeClosure.compute() method does not take any parameters and the return of the computation is what is emitted.
 * The method s() is a shortcut method to this.starts.next().
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ClosurePipe<S, E> extends AbstractPipe<S, E> {

    private final PipeClosure closure;

    public ClosurePipe(final PipeClosure closure) {
        this.closure = closure;
        this.closure.setPipe(this);
    }

    public E processNextStart() {
        return (E) this.closure.compute();
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
