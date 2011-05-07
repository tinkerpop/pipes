package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.MultiIterator;

/**
 * BothEdgesPipe emits the incoming and outgoing edges of a vertex.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BothEdgesPipe extends AbstractEdgesPipe {

    public BothEdgesPipe() {
        super();
    }

    public BothEdgesPipe(final String label) {
        super(label);
    }

    protected Edge processNextStart() {
        while (true) {
            if (null != this.nextEnds && this.nextEnds.hasNext()) {
                return this.nextEnds.next();
            } else {
                final Vertex vertex = this.starts.next();
                if (null == this.label)
                    this.nextEnds = new MultiIterator<Edge>(vertex.getInEdges().iterator(), vertex.getOutEdges().iterator());
                else
                    this.nextEnds = new MultiIterator<Edge>(vertex.getInEdges(this.label).iterator(), vertex.getOutEdges(this.label).iterator());
            }
        }
    }
}