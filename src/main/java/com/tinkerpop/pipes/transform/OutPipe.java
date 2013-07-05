package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;

/**
 * OutPipe will emit vertices that are the tail/source of the outgoing edges to the current vertex.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OutPipe extends VerticesVerticesPipe {

    public OutPipe(final String... labels) {
        super(Direction.OUT, labels);
    }

    public OutPipe(final int branchFactor, final String... labels) {
        super(Direction.OUT, branchFactor, labels);
    }

    public String toString() {
        return (this.branchFactor == Integer.MAX_VALUE) ?
                PipeHelper.makePipeString(this, Arrays.asList(this.labels)) :
                PipeHelper.makePipeString(this, this.branchFactor, Arrays.asList(this.labels));
    }
}
