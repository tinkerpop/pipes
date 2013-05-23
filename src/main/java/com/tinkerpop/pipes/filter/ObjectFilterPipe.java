package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Query;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

/**
 * The ObjectFilterPipe will either allow or disallow all objects that pass through it depending on the result of the compareObject() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final S object;
    private final Query.Compare compare;

    public ObjectFilterPipe(final S object, final Query.Compare compare) {
        this.object = object;
        this.compare = compare;
    }

    protected S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (PipeHelper.compareObjects(this.compare, s, this.object)) {
                return s;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.compare, this.object);
    }
}
