package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BothVerticesPipe extends AbstractPipe<Edge, Vertex> {

    private Vertex next = null;

    protected Vertex processNextStart() {
        if (null == this.next) {
            final Edge edge = this.starts.next();
            this.next = edge.getOutVertex();
            return edge.getInVertex();
        } else {
            final Vertex temp = this.next;
            this.next = null;
            return temp;
        }
    }
}
