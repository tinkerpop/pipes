package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Vertex;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OutPipe extends AbstractEdgesVerticesPipe {

    public OutPipe(final String label) {
        super(label);
    }

    public OutPipe() {
        super();
    }

    public Vertex processNextStart() {
        while (true) {
            if (null == this.nextEnds) {
                final Vertex vertex = this.starts.next();
                if (null != this.label) {
                    this.nextEnds = vertex.getOutEdges(this.label).iterator();
                } else {
                    this.nextEnds = vertex.getOutEdges().iterator();
                }
            } else {
                if (this.nextEnds.hasNext()) {
                    return this.nextEnds.next().getInVertex();
                } else {
                    this.nextEnds = null;
                }
            }
        }
    }
}
