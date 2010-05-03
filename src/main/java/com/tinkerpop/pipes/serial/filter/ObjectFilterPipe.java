package com.tinkerpop.pipes.serial.filter;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The ObjectFilterPipe will either allow or disallow all objects that pass through it that are contained in the provided Collection.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipe<S> extends AbstractFilterPipe<S, S> {

    private final Collection<S> objects;
    private final boolean filter;

    public ObjectFilterPipe(final Collection<S> objects, final boolean filter) {
        this.objects = objects;
        this.filter = filter;
    }

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            if (this.filter) {
                if (!this.doesContain(this.objects, s)) {
                    return s;
                }
            } else {
                if (this.doesContain(this.objects, s)) {
                    return s;
                }
            }
        }
        throw new NoSuchElementException();
    }

}
