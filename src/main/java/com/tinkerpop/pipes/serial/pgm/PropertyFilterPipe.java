package com.tinkerpop.pipes.serial.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.serial.filter.AbstractComparisonFilterPipe;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The PropertyFilterPipe either allows or disallows all Elements that have the provided value(s) for a particular key.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PropertyFilterPipe<S extends Element, T> extends AbstractComparisonFilterPipe<S, T> {

    private final String key;


    public PropertyFilterPipe(String key, final T storedObject, final Filter filter) {
        super(storedObject, filter);
        this.key = key;
    }

    public PropertyFilterPipe(String key, final Collection<T> storedCollection, final Filter filter) {
        super(storedCollection, filter);
        this.key = key;
    }

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S element = this.starts.next();
            if (this.testObjectProperty((T) element.getProperty(this.key))) {
                return element;
            }
        }
        throw new NoSuchElementException();
    }
}
