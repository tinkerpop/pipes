package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.FastNoSuchElementException;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Iterator;

/**
 * GraphQueryPipe makes use of the Graph.query() method in Blueprints which allows for intelligent element look ups from the underlying graph.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GraphQueryPipe<E extends Element> extends QueryPipe<Graph, E> {

    public GraphQueryPipe(final Class<E> elementClass) {
        super.setResultingElementClass(elementClass);
    }

    public E processNextStart() {
        while (true) {
            if (this.count >= this.highRange) {
                throw FastNoSuchElementException.instance();
            } else if (this.currentIterator.hasNext()) {
                this.count++;
                final E e = currentIterator.next();
                if (this.count > this.lowRange)
                    return e;
            } else {
                final Graph graph = this.starts.next();
                GraphQuery query = graph.query();
                if (null != this.hasContainers) {
                    for (final HasContainer hasContainer : this.hasContainers) {
                        query = query.has(hasContainer.key, hasContainer.predicate, hasContainer.value);
                    }
                }
                if (null != this.intervalContainers) {
                    for (final IntervalContainer intervalContainer : this.intervalContainers) {
                        query = query.interval(intervalContainer.key, intervalContainer.startValue, intervalContainer.endValue);
                    }
                }
                if (this.highRange != Integer.MAX_VALUE) {
                    query = query.limit(this.highRange - this.count);
                }

                this.currentIterator = this.elementClass.equals(Vertex.class) ?
                        (Iterator<E>) query.vertices().iterator() :
                        (Iterator<E>) query.edges().iterator();
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, super.toString());
    }
}
