package com.tinkerpop.pipes.serial.sideeffect;

import com.tinkerpop.pipes.serial.Pipe;


/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface SideEffectPipe<S, E, T> extends Pipe<S, E> {

    public T getSideEffect();
}
