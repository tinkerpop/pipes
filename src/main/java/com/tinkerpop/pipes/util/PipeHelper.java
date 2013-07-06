package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.iterators.EmptyIterator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * PipeHelper provides a collection of static methods that are useful when dealing with Pipes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PipeHelper {

    private PipeHelper() {

    }

    /**
     * This will iterate all the objects out of the iterator.
     * This is useful for iterators with side-effect behavior as nothing is returned from the iteration.
     * Note that the try/catch model is not "acceptable Java," but is more efficient given the architecture of AbstractPipe.
     *
     * @param iterator the iterator to drain
     */
    public static <T> void iterate(final Iterator<T> iterator) {
        try {
            while (true) {
                iterator.next();
            }
        } catch (final NoSuchElementException e) {
        }
    }

    /**
     * Drain an iterator into a collection. Useful for storing the results of a Pipe into a collection.
     * Note that the try/catch model is not "acceptable Java," but is more efficient given the architecture of AbstractPipe.
     *
     * @param iterator   the iterator to drain
     * @param collection the collection to fill
     * @param <T>        the object type of the iterator
     */
    public static <T> void fillCollection(final Iterator<T> iterator, final Collection<T> collection) {
        try {
            while (true) {
                collection.add(iterator.next());
            }
        } catch (final NoSuchElementException e) {
        }
    }

    /**
     * Drain an iterator into a collection. Useful for storing the results of a Pipe into a collection.
     * Note that the try/catch model is not "acceptable Java," but is more efficient given the architecture of AbstractPipe.
     *
     * @param iterator   the iterator to drain
     * @param collection the collection to fill
     * @param number     the number of objects to fill into the collection (ordered by iterator)
     * @param <T>        the object type of the iterator
     */
    public static <T> void fillCollection(final Iterator<T> iterator, final Collection<T> collection, final int number) {
        try {
            for (int i = 0; i < number; i++) {
                collection.add(iterator.next());
            }
        } catch (final NoSuchElementException e) {
        }
    }

    /**
     * Drains the iterator into a list that is returned by the method.
     *
     * @param iterator the iterator to drain into the list
     * @param <T>      the object type of the iterator
     * @return a list filled with the objects of the iterator
     */
    public static <T> List<T> makeList(final Iterator<T> iterator) {
        final List<T> list = new ArrayList(50);
        try {
            while (true) {
                list.add(iterator.next());
            }
        } catch (final NoSuchElementException e) {

        }
        return list;
    }

    /**
     * Count the number of objects in an iterator.
     * This will exhaust the iterator.
     * Note that the try/catch model is not "acceptable Java," but is more efficient given the architecture of AbstractPipe.
     *
     * @param iterator the iterator to count
     * @return the number of objects in the iterator
     */
    public static long counter(final Iterator iterator) {
        long counter = 0;
        try {
            while (true) {
                iterator.next();
                counter++;
            }
        } catch (final NoSuchElementException e) {
        }
        return counter;
    }

    /**
     * Checks if the contents of the two iterators are equal and of the same length.
     * Equality is determined using == operator on the internal objects.
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
                result = result + arg + ",";
            }
            result = result.substring(0, result.length() - 1) + ")";
        }
        return result;
    }

    /**
     * Create a PipeFunction for a static method. The method represents PipeFunction.compute().
     *
     * @param method a single argument, static method that will be invoked for when PipeFunction.compute() is called
     * @return a PipeFunction based on the provided compute method
     */
    public static PipeFunction createPipeFunction(final Method method) {

        return new PipeFunction() {
            final Method m = method;

            public Object compute(final Object argument) {
                try {
                    return m.invoke(null, argument);
                } catch (final Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        };
    }

    /**
     * Create a PipeFunction for a static method. The method represents PipeFunction.compute().
     *
     * @param clazz         the class hosting the method
     * @param methodName    the string representation of the method
     * @param argumentTypes the classes of the arguments
     * @return a PipeFunction based on the retrieved compute method
     */
    public static PipeFunction createPipeFunction(final Class clazz, final String methodName, final Class... argumentTypes) {
        try {
            return PipeHelper.createPipeFunction(clazz.getMethod(methodName, argumentTypes));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> Iterator<T> emptyIterator() {
        return (Iterator<T>) EmptyIterator.INSTANCE;
    }


}
