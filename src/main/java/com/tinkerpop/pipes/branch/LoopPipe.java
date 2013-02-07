package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * LoopPipe takes one or two Boolean-based PipeFunctions.
 * For each object of the LoopPipe, the "while" PipeFunction is called.
 * The input of the while PipeFunction is a LoopBundle object which is the object plus some metadata.
 * The boolean of the while PipeFunction determines whether the object should be put back at the start of the LoopPipe or not.
 * The "emit" PipeFunction determines whether the current object should be emitted or not.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopPipe<S> extends AbstractMetaPipe<S, S> implements MetaPipe {

    private final PipeFunction<LoopBundle<S>, Boolean> whileFunction;
    private final PipeFunction<LoopBundle<S>, Boolean> emitFunction;
    private final Pipe<S, S> pipe;
    private ExpandableLoopBundleIterator<S> expando;

    public LoopPipe(final Pipe<S, S> pipe, final PipeFunction<LoopBundle<S>, Boolean> whileFunction, final PipeFunction<LoopBundle<S>, Boolean> emitFunction) {
        this.pipe = pipe;
        this.whileFunction = whileFunction;
        this.emitFunction = emitFunction;
    }

    public LoopPipe(final Pipe<S, S> pipe, final PipeFunction<LoopBundle<S>, Boolean> whileFunction) {
        this(pipe, whileFunction, null);
    }

    protected S processNextStart() {
        while (true) {
            final S s = this.pipe.next();
            final LoopBundle<S> loopBundle;
            if (this.pathEnabled)
                loopBundle = new LoopBundle<S>(s, this.getCurrentPath(), this.getLoops());
            else
                loopBundle = new LoopBundle<S>(s, null, this.getLoops());
            if (whileFunction.compute(loopBundle)) {
                this.expando.add(loopBundle);
                if (null != emitFunction && emitFunction.compute(loopBundle))
                    return s;
            } else {
                if (emitFunction == null || emitFunction.compute(loopBundle))
                    return s;
            }
        }
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(this.pipe);
    }

    public void setStarts(final Iterator<S> iterator) {
        this.expando = new ExpandableLoopBundleIterator<S>(iterator);
        this.pipe.setStarts(this.expando);
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipe);
    }

    public int getLoops() {
        return this.expando.getCurrentLoops() + 1;
    }

    public List getCurrentPath() {
        if (this.pathEnabled) {
            final List path = new ArrayList();
            final List currentPath = this.expando.getCurrentPath();
            if (null != currentPath)
                path.addAll(currentPath);
            path.addAll(this.pipe.getCurrentPath());
            return path;
        } else {
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
        }
    }

    public void reset() {
        this.expando.clear();
        super.reset();
    }

    public static class LoopBundle<T> {

        private final List path;
        private final T t;
        private final int loops;

        protected LoopBundle(final T t, final List path, final int loops) {
            this.t = t;
            this.path = path;
            if (null != path) {
                // remove the join object
                this.path.remove(this.path.size() - 1);
            }
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
        private int totalResets = -1;

        public ExpandableLoopBundleIterator(final Iterator<T> iterator) {
            this.iterator = iterator;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public T next() {
            if (this.queue.isEmpty()) {
                this.current = null;
                if (!this.iterator.hasNext()) {
                    this.incrTotalResets();
                }
                return iterator.next();
            } else {
                this.current = this.queue.remove();
                return this.current.getObject();
            }
        }

        public boolean hasNext() {
            if (this.queue.isEmpty() && !this.iterator.hasNext()) {
                this.incrTotalResets();
                return false;
            } else {
                return true;
            }
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

        /*public int getCurrentLoops() {
            if (null == this.current)
                return 1;
            else
                return this.current.getLoops();
        }*/

        public int getCurrentLoops() {
            if (null != this.current) {
                return this.current.getLoops();
            } else {
                if (this.totalResets == -1)
                    return 1;
                else
                    return totalResets;
            }
        }

        private void incrTotalResets() {
            if (totalResets == -1)
                totalResets = 0;
            this.totalResets++;
        }

        public void clear() {
            this.totalResets = -1;
            this.current = null;
            this.queue.clear();
        }
    }


    public static PipeFunction<Object, Boolean> createTrueFunction() {
        return new PipeFunction<Object, Boolean>() {
            public Boolean compute(final Object object) {
                return Boolean.TRUE;
            }
        };
    }

    public static PipeFunction<LoopBundle, Boolean> createLoopsFunction(final int loops) {
        return new PipeFunction<LoopBundle, Boolean>() {
            public Boolean compute(final LoopBundle loopBundle) {
                return loopBundle.getLoops() < loops;
            }
        };
    }
}