package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Predicate;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * QueryPipe consolidates the fields and methods required for both VertexQueryPipe and GraphQueryPipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class QueryPipe<S, E extends Element> extends AbstractPipe<S, E> implements TransformPipe<S, E> {

    protected List<HasContainer> hasContainers = new ArrayList<HasContainer>();
    protected List<IntervalContainer> intervalContainers = new ArrayList<IntervalContainer>();
    protected Class<E> elementClass;
    protected int lowRange = 0;
    protected int highRange = Integer.MAX_VALUE;
    protected int count = 0;

    protected Iterator<E> currentIterator = PipeHelper.emptyIterator();

    public void setResultingElementClass(final Class<? extends Element> elementClass) {
        if (!Vertex.class.isAssignableFrom(elementClass) && !Edge.class.isAssignableFrom(elementClass))
            throw new IllegalArgumentException("The provided element class must be either Vertex or Edge");

        this.elementClass = (Class) elementClass;
    }

    public Class<? extends Element> getResultElementClass() {
        return this.elementClass;
    }

    public void addHasContainer(final HasContainer container) {
        this.hasContainers.add(container);
    }

    public void addIntervalContainer(final IntervalContainer container) {
        this.intervalContainers.add(container);
    }

    public void setHighRange(final int highRange) {
        this.highRange = (highRange == Integer.MAX_VALUE) ? Integer.MAX_VALUE : highRange + 1;
    }

    public void setLowRange(final int lowRange) {
        this.lowRange = (lowRange < 0) ? 0 : lowRange;
    }

    public void reset() {
        super.reset();
        this.currentIterator = PipeHelper.emptyIterator();
        this.count = 0;
    }

    public String toString() {
        StringBuilder extra = new StringBuilder();
        if (null != this.hasContainers && this.hasContainers.size() > 0)
            extra.append("has");
        if (null != this.intervalContainers && this.intervalContainers.size() > 0) {
            if (extra.length() != 0) extra.append(",");
            extra.append("interval");
        }
        if (this.lowRange != 0 || highRange != Integer.MAX_VALUE) {
            if (extra.length() != 0) extra.append(",");
            extra.append("range:[");
            extra.append(this.lowRange);
            extra.append(",");
            extra.append(this.highRange - 1);
            extra.append("]");
        }
        if (extra.length() != 0) extra.append(",");
        extra.append(this.elementClass.getSimpleName().toLowerCase());
        return extra.toString();
    }


    public static class HasContainer {
        public String key;
        public Object value;
        public Predicate predicate;

        public HasContainer(final String key, final Predicate predicate, final Object value) {
            this.key = key;
            this.value = value;
            this.predicate = predicate;
        }
    }

    public static class IntervalContainer {
        public String key;
        public Comparable startValue;
        public Comparable endValue;

        public IntervalContainer(final String key, final Comparable startValue, final Comparable endValue) {
            this.key = key;
            this.startValue = startValue;
            this.endValue = endValue;
        }
    }
}
