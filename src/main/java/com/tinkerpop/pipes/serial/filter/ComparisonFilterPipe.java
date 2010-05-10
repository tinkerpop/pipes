package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.Pipe;

import java.util.Collection;

/**
 * A ComparisonFilterPipe will filter objects that pass through it depending on some implemented criteria.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface ComparisonFilterPipe<S, T> extends Pipe<S, S> {

    public boolean areEqual(T object1, T object2);

    public boolean doesContain(Collection<T> collection, T object);

}
