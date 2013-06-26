package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.CompareRelation;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;
import java.util.Collection;

/**
 * The LabelFilterPipe either allows or disallows all Edges that have the provided label.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelFilterPipe extends AbstractPipe<Edge, Edge> implements FilterPipe<Edge> {

    private final Object label;
    private final CompareRelation compareRelation;

    public LabelFilterPipe(final CompareRelation compareRelation, final Object label) {
        this.label = label;
        this.compareRelation = compareRelation;
    }

    protected Edge processNextStart() {
        while (true) {
            final Edge edge = this.starts.next();
            if (this.compareRelation.compare(edge.getLabel(), this.label))
                return edge;
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.compareRelation, this.label);
    }
}
