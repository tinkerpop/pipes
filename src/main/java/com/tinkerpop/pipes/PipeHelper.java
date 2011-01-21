package com.tinkerpop.pipes;

import java.util.Collection;
import java.util.Iterator;

/**
 * PipeHelper provides a collection of static methods that are useful when dealing with Pipes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PipeHelper {

    /**
     * Drain an iterator into a collection. Useful for storing the results of a Pipe into a collection.
     *
     * @param iterator   the iterator to drain
     * @param collection the collection to fill
     * @param <T>        the object type of the iterator
     */
    public static <T> void fillCollection(final Iterator<T> iterator, final Collection<T> collection) {
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
    }

    /**
     * Count the number of objects in an iterator. Realize that this will exhaust the iterator.
     *
     * @param iterator the iterator to count
     * @param <T>      the type of the iterator
     * @return the number of objects in the iterator
     */
    public static <T> long counter(final Iterator<T> iterator) {
        long counter = 0;
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        return counter;
    }
}
