package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.pipes.filter.AbstractComparisonFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;

import java.util.NoSuchElementException;

/**
 * The LabelFilterPipe either allows or disallows all Edges that have the provided label.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelFilterPipe extends AbstractComparisonFilterPipe<Edge, String> {

    public LabelFilterPipe(final String storedObject, final ComparisonFilterPipe.Filter filter) {
        super(storedObject, filter);
    }

    protected Edge processNextStart() {
        while (this.starts.hasNext()) {
            Edge edge = this.starts.next();
            if (this.compareObjectProperty(edge.getLabel())) {
                return edge;
            }
        }
        throw new NoSuchElementException();
    }
}
