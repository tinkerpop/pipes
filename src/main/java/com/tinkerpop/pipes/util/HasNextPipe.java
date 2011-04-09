package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.MetaPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;
import java.util.NoSuchElementException;
import java.util.Iterator;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @author Darrick Wiebe (http://ofallpossibleworlds.wordpress.com)
 */
public class HasNextPipe<S> extends AbstractPipe<S, Boolean> implements MetaPipe {

    private final boolean hasOtherPipe;
    private Pipe<S, ?> pipe;
    private Iterator iterator;
    private boolean finished = false;

    /**
     * Initialize this pipe to apply its conditions to its source pipe.
     **/
    public HasNextPipe() {
        this.hasOtherPipe = false;
    }

    /**
     * Initialize this pipe to apply its conditions to the given pipe. Each
     * element in the source pipe will be fed into the given pipe
     * individually.
     *
     * @param pipe The pipe that must emit the correct number of elements. The
     * pipe will be reset between each element.
     **/
    public HasNextPipe(Pipe<S, ?> pipe) {
        this.hasOtherPipe = true;
        this.pipe = pipe;
        this.iterator = pipe;
    }

    public Boolean processNextStart() {
        if (this.hasOtherPipe) {
            final S s = this.starts.next();
            this.pipe.reset();
            this.pipe.setStarts(new SingleIterator<S>(s));
        } else if (this.finished) {
            throw new NoSuchElementException();
        } else {
            this.iterator = this.starts;
            this.finished = true;
        }
        if (this.iterator.hasNext()) {
            this.iterator.next();
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public void reset() {
        super.reset();
        this.finished = false;
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(pipe);
    }
}
