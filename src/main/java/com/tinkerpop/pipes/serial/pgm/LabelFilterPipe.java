package com.tinkerpop.pipes.serial.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.pipes.serial.filter.AbstractComparisonFilterPipe;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The LabelFilterPipe either allows or disallows all Edges that have labels that are in the provided Collection.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelFilterPipe extends AbstractComparisonFilterPipe<Edge, String> {

    private final Collection<String> labels;
    private final boolean filter;

    public LabelFilterPipe(final Collection<String> labels, final boolean filter) {
        this.labels = labels;
        this.filter = filter;
    }

    protected Edge processNextStart() {
        while (this.starts.hasNext()) {
            Edge edge = this.starts.next();
            if (this.filter) {
                if (!this.doesContain(this.labels, edge.getLabel())) {
                    return edge;
                }
            } else {
                if (this.doesContain(this.labels, edge.getLabel())) {
                    return edge;
                }
            }
        }
        throw new NoSuchElementException();
    }
}
