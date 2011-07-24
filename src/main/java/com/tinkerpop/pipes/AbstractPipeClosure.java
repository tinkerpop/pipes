package com.tinkerpop.pipes;

/**
 * AbstractPipeClosure provides an implementation of setPipe().
 * The implementation simply sets the local protected field pipe to the provided P pipe parameter.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractPipeClosure<T, P extends Pipe> implements PipeClosure<T, P> {

    protected P pipe;

    public abstract T compute(final Object... objects);

    public void setPipe(final P hostPipe) {
        this.pipe = hostPipe;
    }

}
