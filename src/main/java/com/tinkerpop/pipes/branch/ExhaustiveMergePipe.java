package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * ExhaustiveMergePipe will drain its first internal pipe, then its second, so on until all internal pipes are drained.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExhaustiveMergePipe<S> extends AbstractPipe<S, S> implements MetaPipe {

    private final List<Pipe> pipes;
    int current = 0;
    final int total;

    public ExhaustiveMergePipe(final List<Pipe> pipes) {
        this.pipes = pipes;
        this.total = pipes.size();
    }

    public S processNextStart() {
        while (true) {
            final Pipe pipe = this.pipes.get(this.current);
            if (pipe.hasNext()) {
                return (S) pipe.next();
            } else {
                this.current++;
                if (this.current % this.total == 0) {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    public void reset() {
        for (final Pipe pipe : this.pipes) {
            pipe.reset();
        }
        super.reset();
    }

    public List<Pipe> getPipes() {
        return this.pipes;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }
}
