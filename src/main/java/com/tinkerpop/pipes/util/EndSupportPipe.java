package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;

import java.util.Iterator;

/**
 * EndSupportPipe can be used to wrap another pipe. This is useful for when the wrapped pipe emits and iterator and/or iterable.
 * EndSupportPipe will emit the objects of the iterable/iterator one at a time.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class EndSupportPipe<S, E> extends AbstractPipe<S, E> {

    private final Pipe<S, E> pipe;
    private Iterator<E> tempIterator;

    public EndSupportPipe(Pipe<S, E> pipe) {
        this.pipe = pipe;
    }

    public E processNextStart() {
        while (true) {
            if (null != this.tempIterator && this.tempIterator.hasNext())
                return this.tempIterator.next();
            else {
                this.pipe.setStarts(new SingleIterator<S>(this.starts.next()));
                if (this.pipe.hasNext()) {
                    Object e = this.pipe.next();
                    if (e instanceof Iterator)
                        this.tempIterator = (Iterator<E>) e;
                    else if (e instanceof Iterable)
                        this.tempIterator = ((Iterable<E>) e).iterator();
                    else
                        return (E) e;
                }


            }
        }
    }
}
