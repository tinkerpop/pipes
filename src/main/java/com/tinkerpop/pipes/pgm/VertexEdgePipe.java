package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.MultiIterator;

import java.util.Iterator;

/**
 * The VertexEdgePipe returns either the incoming or outgoing Edges of the Vertex start.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class VertexEdgePipe extends AbstractPipe<Vertex, Edge> {

    protected Iterator<Edge> nextEnds;
    private final Step step;

    public enum Step {
        OUT_EDGES, IN_EDGES, BOTH_EDGES
    }

    public VertexEdgePipe(final Step step) {
        if (null == step)
            throw new IllegalArgumentException("Step can not be null");
        this.step = step;
    }

    protected Edge processNextStart() {
        while (true) {
            if (null != this.nextEnds && this.nextEnds.hasNext()) {
                return this.nextEnds.next();
            } else {
                switch (this.step) {
                    case OUT_EDGES: {
                        this.nextEnds = this.starts.next().getOutEdges().iterator();
                        break;
                    }
                    case IN_EDGES: {
                        this.nextEnds = this.starts.next().getInEdges().iterator();
                        break;
                    }
                    case BOTH_EDGES: {
                        Vertex vertex = this.starts.next();
                        this.nextEnds = new MultiIterator<Edge>(vertex.getInEdges().iterator(), vertex.getOutEdges().iterator());
                        break;
                    }
                }
            }
        }
    }
}
