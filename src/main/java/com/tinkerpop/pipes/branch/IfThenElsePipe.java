package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.Iterator;

/**
 * IfThenElsePipe will run each incoming S through the provided ifFunction.
 * If the ifFunction returns true, then the S object is passed through the thenFunction.
 * Otherwise, the S object is passed through the elseFunction.
 * If the result of the thenFunction and the elseFunction is an iterable/iterator, it is unrolled.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IfThenElsePipe<S, E> extends AbstractPipe<S, E> {

    private final PipeFunction<S, Boolean> ifFunction;
    private final PipeFunction<S, ?> thenFunction;
    private final PipeFunction<S, ?> elseFunction;
    private Iterator<E> itty = null;

    public IfThenElsePipe(final PipeFunction<S, Boolean> ifFunction, final PipeFunction<S, ?> thenFunction, final PipeFunction<S, ?> elseFunction) {
        this.ifFunction = ifFunction;
        this.thenFunction = thenFunction;
        this.elseFunction = elseFunction;
    }

    public E processNextStart() {
        while (true) {
            if (null != this.itty && this.itty.hasNext()) {
                return this.itty.next();
            } else {
                final S s = this.starts.next();
                if (this.ifFunction.compute(s)) {
                    final Object e = this.thenFunction.compute(s);
                    if (e instanceof Iterable) {
                        this.itty = ((Iterable<E>) e).iterator();
                    } else if (e instanceof Iterator) {
                        this.itty = (Iterator<E>) e;
                    } else {
                        return (E) e;
                    }
                } else {
                    final Object e = this.elseFunction.compute(s);
                    if (e instanceof Iterable) {
                        this.itty = ((Iterable<E>) e).iterator();
                    } else if (e instanceof Iterator) {
                        this.itty = (Iterator<E>) e;
                    } else {
                        return (E) e;
                    }
                }
            }
        }
    }

    public void reset() {
        this.itty = null;
        super.reset();
    }
}
