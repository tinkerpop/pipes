package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractEdgesVerticesPipe extends AbstractPipe<Vertex, Vertex> {

    protected Iterator<Edge> nextEnds;
    protected final String label;

    public AbstractEdgesVerticesPipe(final String label) {
        this.label = label;
    }

    public AbstractEdgesVerticesPipe() {
        this.label = null;
    }

    public String toString() {
        if (null == this.label) {
            return super.toString();
        } else {
            return super.toString() + "(" + this.label + ")";
        }
    }

    public void reset() {
        this.nextEnds = null;
        super.reset();
    }
}
