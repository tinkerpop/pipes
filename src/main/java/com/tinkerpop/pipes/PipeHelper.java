package com.tinkerpop.pipes;

import java.util.Collection;
import java.util.Iterator;

/**
 * PipeHelper provides a collection of static methods that are useful when dealing with Pipes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PipeHelper {

    public static <T> void fillCollection(final Iterator<T> iterator, final Collection<T> collection) {
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
    }

    public static <T> int counter(final Iterator<T> iterator) {
        int counter = 0;
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        return counter;
    }
}
