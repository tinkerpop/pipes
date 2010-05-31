package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * A CollectionFilterPipe will take a collection of objects and a Filter.EQUALS or Filter.NOT_EQUALS argument.
 * If an incoming object is contained (or not contained) in the provided collection, then it is emitted (or not emitted).
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CollectionFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S>, ComparisonFilterPipe<S, S> {

    private final Collection<S> storedCollection;
    private final ComparisonFilterPipe.Filter filter;

    public CollectionFilterPipe(final Collection<S> storedCollection, final ComparisonFilterPipe.Filter filter) {
        this.storedCollection = storedCollection;
        if (filter == ComparisonFilterPipe.Filter.EQUALS || filter == ComparisonFilterPipe.Filter.NOT_EQUALS) {
            this.filter = filter;
        } else {
            throw new IllegalArgumentException("The only legal filters are equals and not equals");
        }
    }

    public boolean compareObjectProperty(S objectProperty) {
        if (this.filter == ComparisonFilterPipe.Filter.EQUALS) {
            if (this.storedCollection.contains(objectProperty))
                return true;
        } else {
            if (!this.storedCollection.contains(objectProperty))
                return true;
        }
        return false;
    }


    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            if (this.compareObjectProperty(s)) {
                return s;
            }
        }
        throw new NoSuchElementException();
    }
}
