package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.Pipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * CopySplitPipe takes a number of pipes during construction.
 * Every object pulled through CopySplitPipe is copied to each of the internal pipes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CopySplitPipe<S> extends AbstractMetaPipe<S, S> implements MetaPipe {

    private final List<Pipeline> pipes = new ArrayList<Pipeline>();

    public CopySplitPipe(final List<Pipe> pipes) {
        for (final Pipe pipe : pipes) {
            if (pipe instanceof Pipeline) {
                ((Pipeline) pipe).addPipe(0, new CopyExpandablePipe<S>(this));
                this.pipes.add((Pipeline) pipe);
            } else {
                final Pipeline<S, ?> pipeline = new Pipeline<S, Object>();
                pipeline.addPipe(new CopyExpandablePipe<S>(this));
                pipeline.addPipe(pipe);
                this.pipes.add(pipeline);
            }
        }
    }

    public CopySplitPipe(final Pipe... pipes) {
        this(Arrays.asList(pipes));
    }

    public S processNextStart() {
        final S s = this.starts.next();

        List tempPath = null;
        if (this.pathEnabled)
            tempPath = new ArrayList(this.getCurrentPath());

        for (final Pipeline pipeline : this.pipes) {
            final CopyExpandablePipe<S> temp = (CopyExpandablePipe<S>) pipeline.get(0);
            temp.add(s);
            if (this.pathEnabled)
                temp.addCurrentPath(tempPath);
        }
        return s;
    }

    public List getCurrentPath() {
        final List path = super.getCurrentPath();
        path.remove(path.size() - 1);
        return path;
    }

    public List<Pipe> getPipes() {
        return (List) this.pipes;
    }

    public void enablePath(final boolean enable) {
        this.pathEnabled = enable;
        if (this.starts instanceof Pipe)
            ((Pipe) this.starts).enablePath(enable);
    }


    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }

    private static class CopyExpandablePipe<S> extends AbstractPipe<S, S> {

        private static final Object TOKEN = new Object();

        protected Queue<Object> queue = new LinkedList<Object>();
        protected Queue<List> paths = new LinkedList<List>();

        private CopySplitPipe<S> parentPipe;

        public CopyExpandablePipe(final CopySplitPipe<S> parentPipe) {
            this.parentPipe = parentPipe;
        }

        public S processNextStart() {
            while (true) {
                if (this.queue.isEmpty()) {
                    this.parentPipe.processNextStart();
                } else {
                    if (pathEnabled) {
                        final Object x = this.queue.remove();
                        if (x == TOKEN) {
                            this.paths.remove();
                        } else {
                            return (S) x;
                        }
                    } else {
                        return (S) this.queue.remove();
                    }
                }
            }
        }

        public List getCurrentPath() {
            if (this.pathEnabled) {
                return new ArrayList(this.paths.peek());
            } else
                throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
        }

        public void addCurrentPath(final List path) {
            this.paths.add(path);
        }

        public void add(final S s) {
            this.queue.add(s);
            if (this.pathEnabled)
                this.queue.add(TOKEN);
        }

        public void reset() {
            this.queue.clear();
            super.reset();
        }

        public void enablePath(final boolean enablePath) {
            this.parentPipe.enablePath(enablePath);
            super.enablePath(enablePath);
        }
    }

}
