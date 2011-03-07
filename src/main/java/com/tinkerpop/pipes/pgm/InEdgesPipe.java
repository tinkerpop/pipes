package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class InEdgesPipe extends AbstractEdgesPipe {

    public InEdgesPipe() {
        super();
    }

    public InEdgesPipe(final String label) {
        super(label);
    }

    protected Edge processNextStart() {
        while (true) {
            if (null != this.nextEnds && this.nextEnds.hasNext()) {
                return this.nextEnds.next();
            } else {
                if (null == this.label)
                    this.nextEnds = this.starts.next().getInEdges().iterator();
                else
                    this.nextEnds = this.starts.next().getInEdges(this.label).iterator();
            }
        }
    }
}