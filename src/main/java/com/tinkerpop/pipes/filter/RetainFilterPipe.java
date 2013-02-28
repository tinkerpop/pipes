package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.util.structures.AsMap;

import java.util.Collection;

/**
 * RetainFilterPipe extends CollectionFilterPipe by assuming Filter.EQUAL and thus, "retain/intersect"-semantics.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RetainFilterPipe<S> extends CollectionFilterPipe<S> {

    public RetainFilterPipe(final Collection<S> storedCollection) {
        super(storedCollection, Filter.EQUAL);
    }

    public RetainFilterPipe(final AsMap asMap, final String... namedSteps) {
        super(Filter.EQUAL, asMap, namedSteps);
    }
}
