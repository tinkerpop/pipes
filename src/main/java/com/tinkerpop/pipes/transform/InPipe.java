package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;

/**
 * InPipe will emit vertices that are the head/target of the incoming edges to the current vertex.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class InPipe extends VerticesVerticesPipe {

    public InPipe(final String... labels) {
        super(Direction.IN, labels);
    }

    public InPipe(final int branchFactor, final String... labels) {
        super(Direction.IN, branchFactor, labels);
    }

    public String toString() {
        return (this.branchFactor == Integer.MAX_VALUE) ?
                PipeHelper.makePipeString(this, Arrays.asList(this.labels)) :
                PipeHelper.makePipeString(this, this.branchFactor, Arrays.asList(this.labels));
    }
}
