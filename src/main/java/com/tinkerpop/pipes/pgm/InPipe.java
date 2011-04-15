package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class InPipe extends AbstractPipe<Vertex, Vertex> {

    private final String label;
    private Iterator<Edge> currentIterator = null;

    public InPipe() {
        this.label = null;
    }

    public InPipe(final String label) {
        this.label = label;
    }

    public Vertex processNextStart() {
        while (true) {
            if (null == this.currentIterator) {
                final Vertex vertex = this.starts.next();
                if (null != this.label) {
                    this.currentIterator = vertex.getInEdges(this.label).iterator();
                } else {
                    this.currentIterator = vertex.getInEdges().iterator();
                }
            } else {
                if (this.currentIterator.hasNext()) {
                    return this.currentIterator.next().getOutVertex();
                } else {
                    this.currentIterator = null;
                }
            }
        }
    }
}
