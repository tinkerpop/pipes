package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Iterator;
import java.util.Map;

/**
 * ScatterPipe will unroll any iterator/iterable/map that is provided to it.
 * This will only occur for one level deep. In other words, it will not unroll an iterator within an iterator.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ScatterPipe<S, E> extends AbstractPipe<S, E> implements TransformPipe<S, E> {

    private Iterator<E> tempIterator = PipeHelper.emptyIterator();

    public E processNextStart() {
        while (true) {
            if (this.tempIterator.hasNext()) {
                return this.tempIterator.next();
            } else {
                final Object object = this.starts.next();
                if (object instanceof Iterator)
                    this.tempIterator = (Iterator<E>) object;
                else if (object instanceof Iterable)
                    this.tempIterator = ((Iterable<E>) object).iterator();
                else if (object instanceof Map)
                    this.tempIterator = (Iterator<E>) ((Map) object).entrySet().iterator();
                else
                    return (E) object;
            }
        }
    }

    public void reset() {
        this.tempIterator = PipeHelper.emptyIterator();
        super.reset();
    }
}
