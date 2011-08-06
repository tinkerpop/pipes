package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;


/**
 * The CountPipe produces a side effect that is the total number of objects that have passed through it.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CountPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, Long> {

    private Long counter = 0l;

    protected S processNextStart() {
        final S s = this.starts.next();
        this.counter++;
        return s;
    }

    public Long getSideEffect() {
        return this.counter;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.counter);
    }

    public void reset() {
        this.counter = 0l;
        super.reset();
    }
}
