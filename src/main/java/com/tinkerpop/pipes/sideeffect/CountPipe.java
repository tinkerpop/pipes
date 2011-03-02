package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;


/**
 * The CountPipe produces a side effect that is the total number of objects that have passed through it.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CountPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, Long> {

    private Long counter = 0l;

  	@Override
  	public boolean hasNext() {
  		return this.starts.hasNext();
  	}

    protected S processNextStart() {
        S s = this.starts.next();
        this.counter++;
        return s;
    }

    public Long getSideEffect() {
        return this.counter;
    }

    public String toString() {
        return super.toString() + "<" + this.counter + ">";
    }
}
