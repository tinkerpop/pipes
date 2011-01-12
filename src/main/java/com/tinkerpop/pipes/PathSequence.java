package com.tinkerpop.pipes;

import java.util.Iterator;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathSequence implements Iterable<List>, Iterator<List> {

    private final Pipe pipe;

    public PathSequence(final Pipe pipe) {
        this.pipe = pipe;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Iterator<List> iterator() {
        return this;
    }

    public List next() {
        this.pipe.next();
        return this.pipe.getPath();
    }

    public boolean hasNext() {
        return this.pipe.hasNext();
    }
}

