package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Query;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;

/**
 * The PropertyFilterPipe either allows or disallows all Elements that have the provided value for a particular key.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PropertyFilterPipe<S extends Element, T> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final String key;
    private final T[] values;
    private final Query.Compare compare;


    public PropertyFilterPipe(final String key, final Query.Compare compare, final T... values) {
        this.key = key;
        if ((null == values || values.length == 0) && (compare.equals(Query.Compare.EQUAL) || compare.equals(Query.Compare.NOT_EQUAL))) {
            this.values = (T[]) new Object[]{null};
            this.compare = compare.opposite();

        } else {
            this.values = values;
            this.compare = compare;
        }
    }

    protected S processNextStart() {
        while (true) {
            final S element = this.starts.next();
            if (PipeHelper.compareObjectArray(this.compare, element.getProperty(this.key), this.values)) {
                return element;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.key, this.compare, Arrays.asList(this.values));
    }

    public String getKey() {
        return this.key;
    }

    public T[] getValues() {
        return this.values;
    }

    public Query.Compare getCompare() {
        return this.compare;
    }

}
