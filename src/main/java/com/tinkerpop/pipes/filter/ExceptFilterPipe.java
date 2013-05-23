package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Query;
import com.tinkerpop.pipes.util.structures.AsMap;

import java.util.Collection;

/**
 * ExceptFilterPipe extends CollectionFilterPipe by assuming Compare.NOT_EQUAL and thus, "except/disjoint-union"-semantics.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExceptFilterPipe<S> extends CollectionFilterPipe<S> {

    public ExceptFilterPipe(final Collection<S> storedCollection) {
        super(storedCollection, Query.Compare.NOT_EQUAL);
    }

    public ExceptFilterPipe(final AsMap asMap, final String... namedSteps) {
        super(Query.Compare.NOT_EQUAL, asMap, namedSteps);
    }
}
