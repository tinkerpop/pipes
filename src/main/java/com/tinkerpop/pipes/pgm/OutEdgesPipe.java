package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OutEdgesPipe extends AbstractEdgesPipe {

    public OutEdgesPipe() {
        super();
    }

    public OutEdgesPipe(final String label) {
        super(label);
    }

    protected Edge processNextStart() {
        while (true) {
            if (null != this.nextEnds && this.nextEnds.hasNext()) {
                return this.nextEnds.next();
            } else {
                if (null == label)
                    this.nextEnds = this.starts.next().getOutEdges().iterator();
                else
                    this.nextEnds = this.starts.next().getOutEdges(this.label).iterator();
            }
        }
    }
}