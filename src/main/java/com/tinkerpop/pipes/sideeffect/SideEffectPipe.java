package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.Pipe;


/**
 * A SideEffectPipe will produce a side effect which can be retrieved by the getSideEffect() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface SideEffectPipe<S, T> extends Pipe<S, S> {

    public T getSideEffect();

    public interface LazySideEffectPipe<S, T> extends SideEffectPipe<S, T> {
    }

    public interface GreedySideEffectPipe<S, T> extends SideEffectPipe<S, T> {
    }

}
