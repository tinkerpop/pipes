package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * An AsPipe wraps a Pipe and provides it a name and 'peak back' access to the last emitted end.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AsPipe<S, E> extends AbstractMetaPipe<S, E> implements MetaPipe {

    private final String name;
    private final Pipe<S, E> pipe;

    public AsPipe(final String name, final Pipe<S, E> pipe) {
        this.pipe = pipe;
        this.name = name;
    }

    public void setStarts(final Iterator<S> starts) {
        this.pipe.setStarts(starts);
        this.starts = starts;
    }

    protected List getPathToHere() {
        return this.pipe.getCurrentPath();
    }

    public E getCurrentEnd() {
        return this.currentEnd;
    }

    public String getName() {
        return this.name;
    }

    public E processNextStart() {
        return this.pipe.next();
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(this.pipe);
    }


    public String toString() {
        return PipeHelper.makePipeString(this, this.name, this.pipe);
    }
}
