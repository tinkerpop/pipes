package com.tinkerpop.pipes.filter;

import java.util.NoSuchElementException;

/**
 * The ObjectFilterPipe will either allow or disallow all objects that pass through it depending on the result of the compareObjectProperty() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipe<S> extends AbstractComparisonFilterPipe<S, S> {

    private final S object;

    public ObjectFilterPipe(final S object, final Filter filter) {
        super(filter);
        this.object = object;
    }

    protected S processNextStart() {
        while (true) {
            S s = this.starts.next();
            if (this.compareObjects(this.object, s)) {
                return s;
            }
        }
    }
}
