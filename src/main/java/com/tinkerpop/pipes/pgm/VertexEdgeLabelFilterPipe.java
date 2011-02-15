package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.MultiIterator;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class VertexEdgeLabelFilterPipe extends AbstractPipe<Vertex, Edge> {

    protected Iterator<Edge> nextEnds;
    private final VertexEdgePipe.Step step;
    private final String label;

    public VertexEdgeLabelFilterPipe(final VertexEdgePipe.Step step, final String label) {
        if (null == step)
            throw new IllegalArgumentException("Step can not be null");
        this.step = step;
        this.label = label;
    }

    protected Edge processNextStart() {
        while (true) {
            if (null != this.nextEnds && this.nextEnds.hasNext()) {
                return this.nextEnds.next();
            } else {
                switch (this.step) {
                    case OUT_EDGES: {
                        this.nextEnds = this.starts.next().getOutEdges(this.label).iterator();
                        break;
                    }
                    case IN_EDGES: {
                        this.nextEnds = this.starts.next().getInEdges(this.label).iterator();
                        break;
                    }
                    case BOTH_EDGES: {
                        Vertex vertex = this.starts.next();
                        this.nextEnds = new MultiIterator<Edge>(vertex.getInEdges(this.label).iterator(), vertex.getOutEdges(this.label).iterator());
                        break;
                    }
                }
            }
        }
    }

    public String toString() {
        return super.toString() + "<" + this.step + "," + this.label + ">";
    }
}
