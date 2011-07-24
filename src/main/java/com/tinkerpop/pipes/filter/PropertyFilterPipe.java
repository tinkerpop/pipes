package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.pgm.Element;

/**
 * The PropertyFilterPipe either allows or disallows all Elements that have the provided value for a particular key.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PropertyFilterPipe<S extends Element, T> extends AbstractComparisonFilterPipe<S, T> {

    private final String key;
    private final T value;


    public PropertyFilterPipe(String key, final T value, final Filter filter) {
        super(filter);
        this.key = key;
        this.value = value;
    }

    protected S processNextStart() {
        while (true) {
            S element = this.starts.next();
            if (!this.compareObjects((T) element.getProperty(this.key), this.value)) {
                return element;
            }
        }
    }

    public String toString() {
        return super.toString() + "(" + this.key + "," + this.filter + "," + this.value + ")";
    }
}
