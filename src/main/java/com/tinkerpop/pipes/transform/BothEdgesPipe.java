package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;

/**
 * BothEdgesPipe emits both the outgoing and incoming edges of a vertex.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BothEdgesPipe extends VerticesEdgesPipe {

    public BothEdgesPipe(final String... labels) {
        super(Direction.BOTH, labels);
    }

    public BothEdgesPipe(final int branchFactor, final String... labels) {
        super(Direction.BOTH, branchFactor, labels);
    }

    public String toString() {
        return (this.branchFactor == Integer.MAX_VALUE) ?
                PipeHelper.makePipeString(this, Arrays.asList(this.labels)) :
                PipeHelper.makePipeString(this, this.branchFactor, Arrays.asList(this.labels));
    }
}
