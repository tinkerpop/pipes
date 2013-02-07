package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.FastNoSuchElementException;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.List;

/**
 * ExhaustiveMergePipe will drain its first internal pipe, then its second, so on until all internal pipes are drained.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExhaustMergePipe<S> extends AbstractMetaPipe<S, S> implements MetaPipe {

    private final List<Pipe> pipes;
    int current = 0;
    final int total;

    public ExhaustMergePipe(final List<Pipe> pipes) {
        this.pipes = pipes;
        this.total = pipes.size();
    }

    public S processNextStart() {
        while (true) {
            final Pipe pipe = this.pipes.get(this.current);
            if (pipe.hasNext()) {
                return (S) pipe.next();
            } else {
                this.current = (this.current + 1) % this.total;
                if (this.current == 0) {
                    throw FastNoSuchElementException.instance();
                }
            }
        }
    }

    public List getCurrentPath() {
        if (this.pathEnabled)
            return this.pipes.get(this.current).getCurrentPath();
        else
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
    }

    public List<Pipe> getPipes() {
        return this.pipes;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }
}