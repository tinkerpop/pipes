package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Compare;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Query;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;

/**
 * The LabelFilterPipe either allows or disallows all Edges that have the provided label.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelFilterPipe extends AbstractPipe<Edge, Edge> implements FilterPipe<Edge> {

    private final String[] labels;
    private final Compare compare;

    public LabelFilterPipe(final Compare compare, final String... labels) {
        this.labels = labels;
        this.compare = compare;
    }

    protected Edge processNextStart() {
        while (true) {
            final Edge edge = this.starts.next();
            if (PipeHelper.compareObjectArray(this.compare, edge.getLabel(), this.labels)) {
                return edge;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.compare, Arrays.asList(this.labels));
    }
}
