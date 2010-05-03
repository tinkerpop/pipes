package com.tinkerpop.pipes.serial.sideeffect;

import com.tinkerpop.pipes.serial.Pipe;


/**
 * A SideEffectPipe will produce a side effect which can be retrieved by the getSideEffect() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface SideEffectPipe<S, E, T> extends Pipe<S, E> {

    public T getSideEffect();
}
