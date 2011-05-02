package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Vertex;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class InPipe extends AbstractEdgesVerticesPipe {

    public InPipe(final String label) {
        super(label);
    }

    public InPipe() {
        super();
    }

    public Vertex processNextStart() {
        while (true) {
            if (null == this.nextEnds) {
                final Vertex vertex = this.starts.next();
                if (null != this.label) {
                    this.nextEnds = vertex.getInEdges(this.label).iterator();
                } else {
                    this.nextEnds = vertex.getInEdges().iterator();
                }
            } else {
                if (this.nextEnds.hasNext()) {
                    return this.nextEnds.next().getOutVertex();
                } else {
                    this.nextEnds = null;
                }
            }
        }
    }
}
