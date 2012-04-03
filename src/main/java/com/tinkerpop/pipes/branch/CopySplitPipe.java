package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.Pipeline;

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

    private final List<Pipeline> pipes = new LinkedList<Pipeline>();

    public CopySplitPipe(final List<Pipe> pipes) {
        for (final Pipe pipe : pipes) {
            final Pipeline<S, ?> pipeline = new Pipeline<S, Object>();
            pipeline.addPipe(new CopyExpandablePipe<S>(this));
            pipeline.addPipe(pipe);
            this.pipes.add(pipeline);
        }
    }

    public CopySplitPipe(final Pipe... pipes) {
        this(Arrays.asList(pipes));
    }

    public S processNextStart() {
        final S s = this.starts.next();

        List tempPath = null;
        if (this.pathEnabled)
            tempPath = this.getCurrentPath();

        for (final Pipeline pipeline : this.pipes) {
            final CopyExpandablePipe<S> temp = (CopyExpandablePipe<S>) pipeline.get(0);
            temp.add(s);
            if (this.pathEnabled)
                temp.addCurrentPath(new LinkedList(tempPath));
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

    private class CopyExpandablePipe<S> extends AbstractPipe<S, S> {

        protected Queue<S> queue = new LinkedList<S>();
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
                    return this.queue.remove();
                }
            }
        }

        public List getCurrentPath() {
            if (this.pathEnabled)
                return this.paths.remove();
            else
                throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
        }

        public void addCurrentPath(final List path) {
            this.paths.add(path);
        }

        public void add(S s) {
            this.queue.add(s);
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
