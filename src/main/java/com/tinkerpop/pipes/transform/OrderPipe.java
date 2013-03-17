package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.FastNoSuchElementException;
import com.tinkerpop.pipes.util.structures.ArrayQueue;
import com.tinkerpop.pipes.util.structures.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * OrderPipe supports in-stream sorting of objects.
 * If no provided sorting function is provided, then a default sort order is assumed.
 * Moreover, when no sorting function is provided, the object S is assumed to be comparable.
 * If a sorting function is provided, then sort is determined by the comparison computed by the function.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrderPipe<S> extends AbstractPipe<S, S> implements TransformPipe<S, S> {

    private final ArrayQueue<ObjectBundle<S>> bundles = new ArrayQueue<ObjectBundle<S>>();
    private final ArrayQueue<S> objects = new ArrayQueue<S>();
    private Comparator<ObjectBundle<S>> bundledComparator;
    private Comparator<S> objectComparator;

    private List currentPath;

    public OrderPipe(final PipeFunction<Pair<S, S>, Integer> compareFunction) {
        this.objectComparator = new PipeFunctionComparator(compareFunction);
        this.bundledComparator = new PipeFunctionBundleComparator(compareFunction);
    }

    public OrderPipe() {
        this(Order.INCR);
    }

    public OrderPipe(final Order order) {
        if (order.equals(Order.INCR)) {
            this.objectComparator = new PipeFunctionComparator(new IncrementFunction());
            this.bundledComparator = new PipeFunctionBundleComparator(new IncrementFunction());
        } else {
            this.objectComparator = new PipeFunctionComparator(new DecrementFunction());
            this.bundledComparator = new PipeFunctionBundleComparator(new DecrementFunction());
        }
    }

    public void enablePath(boolean enablePath) {
        if (enablePath)
            this.objectComparator = null;
        else
            this.bundledComparator = null;
        super.enablePath(enablePath);
    }

    public S processNextStart() {
        while (true) {
            if (this.pathEnabled) {
                if (this.bundles.isEmpty()) {
                    if (!this.starts.hasNext())
                        throw FastNoSuchElementException.instance();
                    else {
                        this.bundles.clear();
                        try {
                            while (true) {
                                this.bundles.add(new ObjectBundle<S>(this.starts.next(), this.getPathToHere()));
                            }
                        } catch (final NoSuchElementException e) {
                        }
                        if (null != this.bundledComparator)
                            Collections.sort(this.bundles, this.bundledComparator);
                        else
                            Collections.sort(this.bundles);
                    }
                } else {
                    final ObjectBundle<S> object = this.bundles.remove();
                    this.currentPath = object.path;
                    return object.object;
                }
            } else {
                if (this.objects.isEmpty()) {
                    if (!this.starts.hasNext())
                        throw FastNoSuchElementException.instance();
                    else {
                        this.objects.clear();
                        try {
                            while (true) {
                                this.objects.add(this.starts.next());
                            }
                        } catch (final NoSuchElementException e) {
                        }
                        if (null != this.objectComparator)
                            Collections.sort(this.objects, this.objectComparator);
                        else
                            Collections.sort((List<Comparable>) this.objects);
                    }
                } else {
                    return this.objects.remove();
                }
            }
        }
    }

    public List getCurrentPath() {
        if (this.pathEnabled) {
            final List pathElements = new ArrayList(this.currentPath);
            final int size = pathElements.size();
            if (this instanceof TransformPipe) {
                pathElements.add(this.currentEnd);
            } else if (size == 0 || pathElements.get(size - 1) != this.currentEnd) {
                // do not repeat filters or side-effects as they dup the object
                pathElements.add(this.currentEnd);
            }
            return pathElements;
        } else {
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
        }
    }


    private class ObjectBundle<S> implements Comparable<ObjectBundle<S>> {

        public final S object;
        public final List path;

        public ObjectBundle(final S object, final List path) {
            this.object = object;
            this.path = path;
        }

        public int compareTo(final ObjectBundle bundle) {
            return ((Comparable) this.object).compareTo(bundle.object);
        }
    }

    private class PipeFunctionBundleComparator implements Comparator<ObjectBundle<S>> {

        private final PipeFunction<Pair<S, S>, Integer> pipeFunction;

        public PipeFunctionBundleComparator(final PipeFunction<Pair<S, S>, Integer> pipeFunction) {
            this.pipeFunction = pipeFunction;
        }

        public int compare(final ObjectBundle<S> a, final ObjectBundle<S> b) {
            return this.pipeFunction.compute(new Pair<S, S>(a.object, b.object));
        }
    }

    private class PipeFunctionComparator implements Comparator<S> {

        private final PipeFunction<Pair<S, S>, Integer> pipeFunction;

        public PipeFunctionComparator(final PipeFunction<Pair<S, S>, Integer> pipeFunction) {
            this.pipeFunction = pipeFunction;
        }

        public int compare(final S a, final S b) {
            return this.pipeFunction.compute(new Pair<S, S>(a, b));
        }
    }

    private class IncrementFunction implements PipeFunction<Pair<S, S>, Integer> {
        public Integer compute(Pair<S, S> pair) {
            return ((Comparable) pair.getA()).compareTo(pair.getB());
        }
    }

    private class DecrementFunction implements PipeFunction<Pair<S, S>, Integer> {
        public Integer compute(Pair<S, S> pair) {
            return ((Comparable) pair.getB()).compareTo(pair.getA());
        }
    }
}
