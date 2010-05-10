package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Collection;

/**
 * The AbstractComparisonFilterPipe provides the necessary functionality that is required of most ComparisonFilterPipe implementations.
 * The areEquals() implementation compares two objects with equals().
 * The doesContain() implementation determines if the collection contains() the object.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractComparisonFilterPipe<S, T> extends AbstractPipe<S, S> implements ComparisonFilterPipe<S, T> {

    public boolean areEqual(T object1, T object2) {
        return object1.equals(object2);
    }

    public boolean doesContain(Collection<T> collection, T object) {
        return collection.contains(object);
    }
}
