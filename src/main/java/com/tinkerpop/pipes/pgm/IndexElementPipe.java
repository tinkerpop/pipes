package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.pipes.AbstractPipe;

import java.util.Iterator;

/**
 * The IndexElementPipe pulls elements our of a graph index and emits them from the pipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IndexElementPipe<E extends Element> extends AbstractPipe<Index, E> {

    protected Iterator<E> nextEnds;
    private final String key;
    private final Object value;

    public IndexElementPipe(final String key, final Object value) {
        this.key = key;
        this.value = value;
    }


    protected E processNextStart() {
        if (null != this.nextEnds && this.nextEnds.hasNext()) {
            return this.nextEnds.next();
        } else {
            this.nextEnds = (Iterator<E>) this.starts.next().get(this.key, this.value).iterator();
        }
        return this.processNextStart();
    }
}
