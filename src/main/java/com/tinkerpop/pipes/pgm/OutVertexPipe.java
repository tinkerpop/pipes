package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OutVertexPipe extends AbstractPipe<Edge, Vertex> {
    protected Vertex processNextStart() {
        return this.starts.next().getOutVertex();
    }
}
