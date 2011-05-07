package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * LabelPipe emits the label of an edge.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelPipe extends AbstractPipe<Edge, String> {

    protected String processNextStart() {
        return this.starts.next().getLabel();
    }
}
