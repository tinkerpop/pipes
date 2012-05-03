package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrderPipe<S> extends AbstractPipe<S, S> {

    private final LinkedList<ObjectBundle<S>> objects = new LinkedList<ObjectBundle<S>>();
    private List currentPath;
    private final Comparator<ObjectBundle<S>> comparator;

    public OrderPipe(final PipeFunction<Pair<S, S>, Integer> compareFunction) {
        this.comparator = new PipeFunctionComparator(compareFunction);
    }

    public OrderPipe() {
        this.comparator = null;
    }

    public S processNextStart() {
        while (true) {
            if (this.objects.isEmpty()) {
                if (!this.starts.hasNext())
                    throw new NoSuchElementException();
                else {
                    this.objects.clear();
                    try {
                        while (true) {
                            final S s = this.starts.next();
                            if (this.pathEnabled)
                                this.objects.add(new ObjectBundle<S>(s, this.getPathToHere()));
                            else
                                this.objects.add(new ObjectBundle<S>(s, null));
                        }
                    } catch (final NoSuchElementException e) {
                    }
                    if (null != comparator)
                        Collections.sort(this.objects, this.comparator);
                    else
                        Collections.sort(this.objects);
                }
            } else {
                final ObjectBundle<S> object = this.objects.remove();
                this.currentPath = object.path;
                return object.object;
            }
        }
    }

    public List getCurrentPath() {
        if (this.pathEnabled) {
            final List pathElements = new ArrayList(this.currentPath);
            final int size = pathElements.size();
            // do not repeat filters as they dup the object
            if (size == 0 || pathElements.get(size - 1) != this.currentEnd) {
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

    private class PipeFunctionComparator implements Comparator<ObjectBundle<S>> {

        private final PipeFunction<Pair<S, S>, Integer> pipeFunction;

        public PipeFunctionComparator(final PipeFunction<Pair<S, S>, Integer> pipeFunction) {
            this.pipeFunction = pipeFunction;
        }

        public int compare(final ObjectBundle<S> a, final ObjectBundle<S> b) {
            return this.pipeFunction.compute(new Pair<S, S>(a.object, b.object));
        }
    }
}
