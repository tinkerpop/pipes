package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Collection;

/**
 * A CollectionFilterPipe will take a collection of objects and a Filter.NOT_EQUAL or Filter.EQUAL argument.
 * If an incoming object is contained (or not contained) in the provided collection, then it is emitted (or not emitted).
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class CollectionFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final Collection<S> storedCollection;
    private final FilterPipe.Filter filter;

    public CollectionFilterPipe(final Collection<S> storedCollection, final FilterPipe.Filter filter) {
        this.storedCollection = storedCollection;
        if (filter == Filter.NOT_EQUAL || filter == Filter.EQUAL) {
            this.filter = filter;
        } else {
            throw new IllegalArgumentException("The only legal filters are equals and not equals");
        }
    }


    private boolean checkCollection(final S rightObject) {
        if (this.filter == Filter.NOT_EQUAL) {
            return !this.storedCollection.contains(rightObject);
        } else {
            return this.storedCollection.contains(rightObject);
        }
    }


    protected S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (this.checkCollection(s)) {
                return s;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.filter);
    }
}
