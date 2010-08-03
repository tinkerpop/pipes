package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.filter.AbstractComparisonFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;

import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdFilterPipe extends AbstractComparisonFilterPipe<Element, Object> {

    public IdFilterPipe(final Object storedObject, final ComparisonFilterPipe.Filter filter) {
        super(storedObject, filter);
    }

    protected Element processNextStart() {
        while (this.starts.hasNext()) {
            Element element = this.starts.next();
            if (this.compareObjectProperty(element.getId())) {
                return element;
            }
        }
        throw new NoSuchElementException();
    }
}
