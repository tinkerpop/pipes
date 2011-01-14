package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.filter.AbstractComparisonFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdFilterPipe extends AbstractComparisonFilterPipe<Element, Object> {

    private final Object id;

    public IdFilterPipe(final Object id, final ComparisonFilterPipe.Filter filter) {
        super(filter);
        this.id = id;
    }

    protected Element processNextStart() {
        while (true) {
            Element element = this.starts.next();
            if (!this.compareObjects(element.getId(), this.id)) {
                return element;
            }
        }
    }
}
