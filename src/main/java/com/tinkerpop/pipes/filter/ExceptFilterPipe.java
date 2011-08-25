package com.tinkerpop.pipes.filter;

import java.util.Collection;

/**
 * ExceptFilterPipe extends CollectionFilterPipe by assuming Filter.NOT_EQUAL and thus, "except/disjoint-union"-semantics.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExceptFilterPipe<S> extends CollectionFilterPipe<S> {

    public ExceptFilterPipe(final Collection<S> storedCollection) {
        super(storedCollection, Filter.NOT_EQUAL);
    }
}
