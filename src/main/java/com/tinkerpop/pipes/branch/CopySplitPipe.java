package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;

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

    private final List<Pipe> pipes;
    private final List<CopyExpandablePipe<S>> pipeStarts = new ArrayList<CopyExpandablePipe<S>>();

    public CopySplitPipe(final List<Pipe> pipes) {
        this.pipes = pipes;
        for (final Pipe pipe : this.pipes) {
            CopyExpandablePipe<S> expandable = new CopyExpandablePipe<S>();
            expandable.setStarts(this);
            pipe.setStarts(expandable.iterator());
            this.pipeStarts.add(expandable);
        }
    }

    public CopySplitPipe(final Pipe... pipes) {
        this(Arrays.asList(pipes));
    }

    public S processNextStart() {
        final S s = this.starts.next();
        for (final CopyExpandablePipe expandable : this.pipeStarts) {
            expandable.add(s);
        }
        return s;
    }

    public List<Pipe> getPipes() {
        return this.pipes;
    }

    public void reset() {
        for (final Pipe pipe : this.pipes) {
            pipe.reset();
        }
        super.reset();
    }

    private class CopyExpandablePipe<S> extends AbstractPipe<S, S> {
        private final Queue<S> queue = new LinkedList<S>();

        public S processNextStart() {
            while (true) {
                if (this.queue.isEmpty()) {
                    this.starts.next();
                } else {
                    return this.queue.remove();
                }
            }
        }

        public void add(S s) {
            this.queue.add(s);
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }
}
