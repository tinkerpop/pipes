package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

/**
 * The ObjectFilterPipe will either allow or disallow all objects that pass through it depending on the result of the compareObject() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final S object;
    private final FilterPipe.Filter filter;

    public ObjectFilterPipe(final S object, final Filter filter) {
        this.object = object;
        this.filter = filter;
    }

    protected S processNextStart() {
        while (true) {
            S s = this.starts.next();
            if (PipeHelper.compareObjects(this.filter, s, this.object)) {
                return s;
            }
        }
    }

    public String toString() {
        return super.toString() + "(" + this.object + ")";
    }
}
