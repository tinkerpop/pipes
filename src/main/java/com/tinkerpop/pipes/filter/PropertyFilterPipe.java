package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Predicate;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

/**
 * The PropertyFilterPipe either allows or disallows all Elements that have the provided value for a particular key.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PropertyFilterPipe<S extends Element, T> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final String key;
    private final Object value;
    private final Predicate predicate;

    public PropertyFilterPipe(final String key, final Predicate predicate, final Object value) {
        this.key = key;
        this.value = value;
        this.predicate = predicate;
    }

    protected S processNextStart() {
        while (true) {
            final S element = this.starts.next();
            if (this.predicate.evaluate(element.getProperty(this.key), this.value)) {
                return element;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.key, this.predicate, this.value);
    }

    public String getKey() {
        return this.key;
    }

    public Object getValue() {
        return this.value;
    }

    public Predicate getPredicate() {
        return this.predicate;
    }

}
