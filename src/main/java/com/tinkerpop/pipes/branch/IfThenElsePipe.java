package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;

import java.util.Iterator;

/**
 * IfThenElsePipe will run each incoming S through the provided IfClosure.
 * If the IfClosure return true, then the S object is passed through the thenClosure.
 * Otherwise, the S object is passed through the elseClosure.
 * If the result of the thenClosure and the elseClosure is an iterable/iterator, it is unrolled.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IfThenElsePipe<S, E> extends AbstractPipe<S, E> {

    private final PipeClosure<Boolean, Pipe> ifClosure;
    private final PipeClosure thenClosure;
    private final PipeClosure elseClosure;
    private Iterator<E> itty = null;

    public IfThenElsePipe(final PipeClosure<Boolean, Pipe> ifClosure, final PipeClosure thenClosure, final PipeClosure elseClosure) {
        this.ifClosure = ifClosure;
        this.thenClosure = thenClosure;
        this.elseClosure = elseClosure;
    }

    public IfThenElsePipe(final PipeClosure ifClosure, final PipeClosure thenClosure) {
        this(ifClosure, thenClosure, null);
    }

    public E processNextStart() {
        while (true) {
            if (null != itty && itty.hasNext()) {
                return itty.next();
            } else {
                final S s = this.starts.next();
                if (this.ifClosure.compute(s)) {
                    Object e = this.thenClosure.compute(s);
                    if (e instanceof Iterable) {
                        itty = ((Iterable<E>) e).iterator();
                    } else if (e instanceof Iterator) {
                        itty = (Iterator<E>) e;
                    } else {
                        return (E) e;
                    }
                } else if (null != this.elseClosure) {
                    Object e = this.elseClosure.compute(s);
                    if (e instanceof Iterable) {
                        itty = ((Iterable<E>) e).iterator();
                    } else if (e instanceof Iterator) {
                        itty = (Iterator<E>) e;
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
