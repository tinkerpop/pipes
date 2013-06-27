package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Predicate;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

/**
 * The ObjectFilterPipe will either allow or disallow all objects that pass through it depending on the result of the compareObject() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ObjectFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final S object;
    private final Predicate predicate;

    public ObjectFilterPipe(final S object, final Predicate predicate) {
        this.object = object;
        this.predicate = predicate;
    }

    protected S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (this.predicate.evaluate(s, this.object)) {
                return s;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.predicate, this.object);
    }
}
