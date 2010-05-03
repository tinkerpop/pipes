package com.tinkerpop.pipes.serial.sideeffect;

import com.tinkerpop.pipes.serial.AbstractPipe;


/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CountPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, Long> {

    private Long counter = 0l;

    protected S processNextStart() {
        S s = this.starts.next();
        this.counter++;
        return s;
    }

    public Long getSideEffect() {
        return this.counter;
    }
}
