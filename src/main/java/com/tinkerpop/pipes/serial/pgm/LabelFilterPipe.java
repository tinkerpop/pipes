package com.tinkerpop.pipes.serial.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.pipes.serial.filter.AbstractComparisonFilterPipe;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The LabelFilterPipe either allows or disallows all Edges that have the provided label(s).
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelFilterPipe extends AbstractComparisonFilterPipe<Edge, String> {

    public LabelFilterPipe(final String storedObject, final Filter filter) {
        super(storedObject, filter);
    }

    public LabelFilterPipe(final Collection<String> storedCollection, final Filter filter) {
        super(storedCollection, filter);
    }

    protected Edge processNextStart() {
        while (this.starts.hasNext()) {
            Edge edge = this.starts.next();
            if (this.testObject(edge.getLabel())) {
                return edge;
            }
        }
        throw new NoSuchElementException();
    }
}
