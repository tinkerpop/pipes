package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.FilterPipe;

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

    /**
     * Checks if the contents of the two iterators are equal and of the same length.
     * Equality is determined using == operator on the interal objects.
     *
     * @param ittyA An iterator
     * @param ittyB An iterator
     * @return Returns true if the two iterators contain the same objects and are of the same length
     */
    public static boolean areEqual(final Iterator ittyA, final Iterator ittyB) {
        if (ittyA.hasNext() != ittyB.hasNext())
            return false;

        while (ittyA.hasNext()) {
            if (!ittyB.hasNext())
                return false;
            if (ittyA.next() != ittyB.next())
                return false;
        }
        return true;
    }

    /**
     * Useful for FilterPipes that need to compare two objects given a filter predicate.
     *
     * @param filter      the filter predicate
     * @param leftObject  the first object
     * @param rightObject the second object
     * @return whether the predicate holds over the two provided objects
     */
    public static boolean compareObjects(final FilterPipe.Filter filter, final Object leftObject, final Object rightObject) {
        switch (filter) {
            case EQUAL:
                if (null == leftObject)
                    return rightObject == null;
                return leftObject.equals(rightObject);
            case NOT_EQUAL:
                if (null == leftObject)
                    return rightObject != null;
                return !leftObject.equals(rightObject);
            case GREATER_THAN:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) == 1;
            case LESS_THAN:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) == -1;
            case GREATER_THAN_EQUAL:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) >= 0;
            case LESS_THAN_EQUAL:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) <= 0;
            default:
                throw new IllegalArgumentException("Invalid state as no valid filter was provided");
        }
    }

    /**
     * Generate a String representation of a pipe given the pipe and some arguments of the pipe.
     *
     * @param pipe      the pipe's class.getSimpleName() is used
     * @param arguments arguments used in the configuration of the pipe (please avoid objects with massive toString() representations)
     * @return a String representation of the pipe
     */
    public static String makePipeString(final Pipe pipe, final Object... arguments) {
        String result = pipe.getClass().getSimpleName();
        if (arguments.length > 0) {
            result = result + "(";
            for (final Object arg : arguments) {
                result = result + arg.toString() + ",";
            }
            result = result.substring(0, result.length() - 1) + ")";
        }
        return result;
    }
}
