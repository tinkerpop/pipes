package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.FastNoSuchElementException;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.List;

/**
 * FairMergePipe will, in a round robin fashion, emit the the objects of its internal pipes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FairMergePipe<S> extends AbstractMetaPipe<S, S> implements MetaPipe {

    private final List<Pipe> pipes;
    int current = 0;
    final int total;

    public FairMergePipe(final List<Pipe> pipes) {
        this.pipes = pipes;
        this.total = pipes.size();
    }

    public S processNextStart() {
        int counter = 0;
        while (true) {
            counter++;
            final Pipe currentPipe = this.pipes.get(this.current);
            if (currentPipe.hasNext()) {
                final S s = (S) currentPipe.next();
                this.current = (this.current + 1) % this.total;
                return s;
            } else if (counter == this.total) {
                throw FastNoSuchElementException.instance();
            } else {
                this.current = (this.current + 1) % this.total;
            }
        }
    }

    public List getCurrentPath() {
        if (this.pathEnabled) {
            int tempCurrent = this.current - 1;
            if (tempCurrent < 0)
                tempCurrent = this.total - 1;
            return this.pipes.get(tempCurrent).getCurrentPath();
        } else
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
    }

    public List<Pipe> getPipes() {
        return this.pipes;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }
}