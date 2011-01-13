package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.pipes.filter.AbstractComparisonFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;

/**
 * The LabelFilterPipe either allows or disallows all Edges that have the provided label.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelFilterPipe extends AbstractComparisonFilterPipe<Edge, String> {

    private final String label;

    public LabelFilterPipe(final String label, final ComparisonFilterPipe.Filter filter) {
        super(filter);
        this.label = label;
    }

    protected Edge processNextStart() {
        while (true) {
            Edge edge = this.starts.next();
            if (!this.compareObjects(edge.getLabel(), this.label)) {
                return edge;
            }
        }
    }

    public String toString() {
        return super.toString() + "<" + this.filter + "," + this.label + ">";
    }
}
