package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractEdgesPipe extends AbstractPipe<Vertex, Edge> {

    protected Iterator<Edge> nextEnds;
    protected final String[] labels;

    public AbstractEdgesPipe(final String... labels) {
        this.labels = labels;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.labels);
    }

    public void reset() {
        this.nextEnds = null;
        super.reset();
    }
}
