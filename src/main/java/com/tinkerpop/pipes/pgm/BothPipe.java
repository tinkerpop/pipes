package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.MultiIterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BothPipe extends AbstractEdgesVerticesPipe {

    private Vertex startVertex = null;

    public BothPipe(final String label) {
        super(label);
    }

    public BothPipe() {
        super();
    }

    public Vertex processNextStart() {
        while (true) {
            if (null != this.nextEnds && this.nextEnds.hasNext()) {
                final Edge edge = this.nextEnds.next();
                final Vertex tempOut = edge.getOutVertex();
                final Vertex tempIn = edge.getInVertex();
                if (tempOut.equals(tempIn)) {
                    return tempIn;
                } else if (tempOut.equals(this.startVertex)) {
                    return tempIn;
                } else {
                    return tempOut;
                }
            } else {
                this.startVertex = this.starts.next();
                if (null == this.label)
                    this.nextEnds = new MultiIterator<Edge>(this.startVertex.getInEdges().iterator(), this.startVertex.getOutEdges().iterator());
                else
                    this.nextEnds = new MultiIterator<Edge>(this.startVertex.getInEdges(this.label).iterator(), this.startVertex.getOutEdges(this.label).iterator());
            }
        }
    }

    public void reset() {
        this.startVertex = null;
        super.reset();
    }
}
