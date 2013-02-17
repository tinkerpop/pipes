package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.Pipe;


/**
 * A SideEffectPipe will produce a side effect which can be retrieved by the getSideEffect() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface SideEffectPipe<S, T> extends Pipe<S, S> {

    public T getSideEffect();

    /**
     * Specifies that the SideEffect is ready after each Pipe.next()
     */
    public interface LazySideEffectPipe<S, T> extends SideEffectPipe<S, T> {
    }

    /**
     * Specifies that the SideEffect is ready after Pipe.iterate()
     */
    public interface GreedySideEffectPipe<S, T> extends SideEffectPipe<S, T> {
    }

}
