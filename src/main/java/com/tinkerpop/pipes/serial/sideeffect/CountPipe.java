package com.tinkerpop.pipes.serial.sideeffect;

import com.tinkerpop.pipes.serial.AbstractPipe;


/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CountPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, Long> {

    private Long counter = 0l;

    protected S processNextStart() {
        S tempEnd = this.starts.next();
        counter++;
        return tempEnd;
    }

    public Long getSideEffect() {
        return this.counter;
    }
}
