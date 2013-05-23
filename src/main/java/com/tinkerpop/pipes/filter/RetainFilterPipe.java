package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Query;
import com.tinkerpop.pipes.util.structures.AsMap;

import java.util.Collection;

/**
 * RetainFilterPipe extends CollectionFilterPipe by assuming Compare.EQUAL and thus, "retain/intersect"-semantics.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RetainFilterPipe<S> extends CollectionFilterPipe<S> {

    public RetainFilterPipe(final Collection<S> storedCollection) {
        super(storedCollection, Query.Compare.EQUAL);
    }

    public RetainFilterPipe(final AsMap asMap, final String... namedSteps) {
        super(Query.Compare.EQUAL, asMap, namedSteps);
    }
}
