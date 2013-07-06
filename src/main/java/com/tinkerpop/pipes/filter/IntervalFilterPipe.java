package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Compare;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * IntervalFilterPipe will filter an element flowing through it according to whether a particular property value of the element is within provided range.
 * For those objects who property value for provided key is null, the element is filtered out of the stream.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IntervalFilterPipe<S extends Element> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final String key;
    private final Comparable startValue;
    private final Comparable endValue;

    public IntervalFilterPipe(final String key, final Comparable startValue, final Comparable endValue) {
        this.key = key;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            final Object value = s.getProperty(key);
            if (null == value)
                continue;
            else {
                if (Compare.GREATER_THAN_EQUAL.evaluate(value, this.startValue) && Compare.LESS_THAN.evaluate(value, this.endValue))
                    return s;
            }
        }
    }

    public String getKey() {
        return this.key;
    }

    public Comparable getStartValue() {
        return this.startValue;
    }

    public Comparable getEndValue() {
        return this.endValue;
    }
}
