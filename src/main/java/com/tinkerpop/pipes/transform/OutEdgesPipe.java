package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;

/**
 * OutEdgesPipe emits the outgoing edges of a vertex.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OutEdgesPipe extends VerticesEdgesPipe {

    public OutEdgesPipe(final String... labels) {
        super(Direction.OUT, labels);
    }

    public OutEdgesPipe(final int branchFactor, final String... labels) {
        super(Direction.OUT, branchFactor, labels);
    }

    public String toString() {
        return (this.branchFactor == Integer.MAX_VALUE) ?
                PipeHelper.makePipeString(this, Arrays.asList(this.labels)) :
                PipeHelper.makePipeString(this, this.branchFactor, Arrays.asList(this.labels));
    }
}
