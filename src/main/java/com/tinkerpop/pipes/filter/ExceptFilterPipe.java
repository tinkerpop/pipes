package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Contains;
import com.tinkerpop.pipes.util.structures.AsMap;

import java.util.Collection;

/**
 * ExceptFilterPipe extends CollectionFilterPipe by assuming Compare.NOT_EQUAL and thus, "except/disjoint-union"-semantics.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExceptFilterPipe<S> extends CollectionFilterPipe<S> {

    public ExceptFilterPipe(final Collection<S> storedCollection) {
        super(storedCollection, Contains.NOT_IN);
    }

    public ExceptFilterPipe(final AsMap asMap, final String... namedSteps) {
        super(Contains.NOT_IN, asMap, namedSteps);
    }
}
