package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Predicate;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

/**
 * The LabelFilterPipe either allows or disallows all Edges that have the provided label.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelFilterPipe extends AbstractPipe<Edge, Edge> implements FilterPipe<Edge> {

    private final Object label;
    private final Predicate predicate;

    public LabelFilterPipe(final Predicate predicate, final Object label) {
        this.label = label;
        this.predicate = predicate;
    }

    protected Edge processNextStart() {
        while (true) {
            final Edge edge = this.starts.next();
            if (this.predicate.evaluate(edge.getLabel(), this.label))
                return edge;
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.predicate, this.label);
    }
}
