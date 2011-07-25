package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import com.tinkerpop.pipes.util.MetaPipe;

import java.util.*;

/**
 * LoopPipe takes a Boolean-based PipeClosure.
 * For each object of the LoopPipe, the PipeClosure is called.
 * The first parameter of the PipeClosure is a LoopBundle object which is the object plus some metadata.
 * The PipeClosure returns a Boolean.
 * The Boolean determines whether the object should be put back at the start of the LoopPipe or not.
 * In essence, the semantics of the PipeClosure is "while."
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopPipe<S> extends AbstractPipe<S, S> implements MetaPipe {

    private final PipeClosure<Boolean, Pipe> closure;
    private final Pipe<S, S> pipe;
    private ExpandableLoopBundleIterator<S> expando;

    public LoopPipe(final Pipe<S, S> pipe, final PipeClosure<Boolean, Pipe> closure) {
        this.pipe = pipe;
        this.closure = closure;
        this.closure.setPipe(this);
    }

    protected S processNextStart() {
        while (true) {
            final S s = this.pipe.next();
            final LoopBundle<S> loopBundle = new LoopBundle<S>(s, this.getPath(), this.getLoops());
            if (closure.compute(loopBundle)) {
                this.expando.add(loopBundle);
            } else {
                return s;
            }
        }
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(pipe);
    }

    public void setStarts(final Iterator<S> iterator) {
        this.expando = new ExpandableLoopBundleIterator<S>(iterator);
        this.pipe.setStarts(this.expando);
    }

    public String toString() {
        return super.toString() + "[" + this.pipe + "]";
    }

    public int getLoops() {
        return this.expando.getCurrentLoops() + 1;
    }

    public List getPath() {
        final List path = new ArrayList();
        final List currentPath = this.expando.getCurrentPath();
        if (null != currentPath)
            path.addAll(currentPath);
        path.addAll(this.pipe.getPath());
        return path;
    }

    public void reset() {
        this.expando.clear();
        this.pipe.reset();
        super.reset();
    }

    public class LoopBundle<T> {

        private final List path;
        private final T t;
        private final int loops;

        protected LoopBundle(final T t, final List path, final int loops) {
            this.t = t;
            this.path = path;
            // remove the join object
            this.path.remove(this.path.size() - 1);
            this.loops = loops;
        }

        public List getPath() {
            return this.path;
        }

        public int getLoops() {
            return this.loops;
        }

        public T getObject() {
            return this.t;
        }
    }

    private class ExpandableLoopBundleIterator<T> implements Iterator<T> {

        private final Queue<LoopBundle<T>> queue = new LinkedList<LoopBundle<T>>();
        private final Iterator<T> iterator;
        private LoopBundle<T> current;

        public ExpandableLoopBundleIterator(final Iterator<T> iterator) {
            this.iterator = iterator;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public T next() {
            if (this.queue.isEmpty()) {
                return iterator.next();
            } else {
                this.current = this.queue.remove();
                return this.current.getObject();
            }
        }

        public boolean hasNext() {
            return !this.queue.isEmpty() || this.iterator.hasNext();
        }

        public void add(final LoopBundle<T> loopBundle) {
            this.queue.add(loopBundle);
        }

        public List getCurrentPath() {
            if (null == this.current)
                return null;
            else
                return this.current.getPath();

        }

        public int getCurrentLoops() {
            if (null == this.current)
                return 1;
            else
                return this.current.getLoops();
        }

        public void clear() {
            this.current = null;
            this.queue.clear();
        }
    }

}
