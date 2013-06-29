package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * LabelPipe emits the label of an edge.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelPipe extends AbstractPipe<Edge, String> implements TransformPipe<Edge, String> {

    protected String processNextStart() {
        return this.starts.next().getLabel();
    }
}
