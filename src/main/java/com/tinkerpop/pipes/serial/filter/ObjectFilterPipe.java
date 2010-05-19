package com.tinkerpop.pipes.serial.filter;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The ObjectFilterPipe will either allow or disallow all objects that pass through it that pass the testObjectProperty() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipe<S> extends AbstractComparisonFilterPipe<S, S> {


    public ObjectFilterPipe(final S storedObject, final Filter filter) {
        super(storedObject, filter);
    }

    public ObjectFilterPipe(final Collection<S> storedCollection, final Filter filter) {
        super(storedCollection, filter);
    }

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            if (this.testObjectProperty(s)) {
                return s;
            }
        }
        throw new NoSuchElementException();
    }
}
