package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Collection;

/**
 * The AbstractFilterPipe provides the necessary functionality that is required of most FilterPipe implementations.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractFilterPipe<S,T> extends AbstractPipe<S, S> implements FilterPipe<S,T> {

    public boolean areEqual(T object1, T object2) {
        return object1.equals(object2);
    }

    public boolean doesContain(Collection<T> collection, T object) {
        return collection.contains(object);
    }
}
