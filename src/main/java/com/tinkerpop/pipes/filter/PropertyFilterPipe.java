package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Compare;
import com.tinkerpop.blueprints.CompareRelation;
import com.tinkerpop.blueprints.Element;
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
    private final Object[] values;
    private final CompareRelation compareRelation;

    public PropertyFilterPipe(final String key, final CompareRelation compare, final Object... values) {
        this.key = key;
        if ((null == values || values.length == 0) && (compare.equals(Compare.EQUAL) || compare.equals(Compare.NOT_EQUAL))) {
            this.values = new Object[]{null};
            this.compareRelation = ((Compare) compare).opposite();

        } else {
            this.values = values;
            this.compareRelation = compare;
        }
    }

    protected S processNextStart() {
        while (true) {
            final S element = this.starts.next();
            if (PipeHelper.compareObjectArray((Compare) this.compareRelation, element.getProperty(this.key), this.values)) {
                return element;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.key, this.compareRelation, Arrays.asList(this.values));
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getValues() {
        return this.values;
    }

    public CompareRelation getCompareRelation() {
        return this.compareRelation;
    }

}
