package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

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
        OUT_EDGES, IN_EDGES
    }

    public VertexEdgePipe(final Step step) {
        this.step = step;
    }

    protected Edge processNextStart() {
        if (null != this.nextEnds && this.nextEnds.hasNext()) {
            return this.nextEnds.next();
        } else {
            if (this.step.equals(Step.OUT_EDGES))
                this.nextEnds = this.starts.next().getOutEdges().iterator();
            else
                this.nextEnds = this.starts.next().getInEdges().iterator();
            return this.processNextStart();
        }
    }
}