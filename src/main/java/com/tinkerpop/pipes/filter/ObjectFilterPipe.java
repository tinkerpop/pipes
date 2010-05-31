package com.tinkerpop.pipes.filter;

import java.util.NoSuchElementException;

/**
 * The ObjectFilterPipe will either allow or disallow all objects that pass through it depending on the result of the compareObjectProperty() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipe<S> extends AbstractComparisonFilterPipe<S, S> {


    public ObjectFilterPipe(final S storedObject, final Filter filter) {
        super(storedObject, filter);
    }

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            if (this.compareObjectProperty(s)) {
                return s;
            }
        }
        throw new NoSuchElementException();
    }
}
