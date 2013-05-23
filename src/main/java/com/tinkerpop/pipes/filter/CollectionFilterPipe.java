package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Query;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.structures.AsMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A CollectionFilterPipe will take a collection of objects and a Filter.NOT_EQUAL or Filter.EQUAL argument.
 * If an incoming object is contained (or not contained) in the provided collection, then it is emitted (or not emitted).
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class CollectionFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final Collection<S> storedCollection;
    private final Query.Compare compare;

    public CollectionFilterPipe(final Collection<S> storedCollection, final Query.Compare compare) {
        this.storedCollection = storedCollection;
        if (compare == Query.Compare.NOT_EQUAL || compare == Query.Compare.EQUAL) {
            this.compare = compare;
        } else {
            throw new IllegalArgumentException("The only legal filters are equals and not equals");
        }
    }

    public CollectionFilterPipe(final Query.Compare compare, final AsMap asMap, final String... namedSteps) {
        this.storedCollection = new DynamicList<S>(asMap, namedSteps);
        if (compare == Query.Compare.NOT_EQUAL || compare == Query.Compare.EQUAL) {
            this.compare = compare;
        } else {
            throw new IllegalArgumentException("The only legal filters are equals and not equals");
        }
    }


    private boolean checkCollection(final S rightObject) {
        if (this.compare == Query.Compare.NOT_EQUAL) {
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
        if (this.storedCollection instanceof DynamicList)
            return PipeHelper.makePipeString(this, this.compare, ((DynamicList) this.storedCollection).toString());
        else
            return PipeHelper.makePipeString(this, this.compare);
    }

    private class DynamicList<S> extends ArrayList<S> {

        private final AsMap asMap;
        private final String[] namedSteps;

        public DynamicList(final AsMap asMap, final String... namedSteps) {
            this.asMap = asMap;
            this.namedSteps = namedSteps;
        }

        @Override
        public boolean contains(final Object object) {
            for (final String namedStep : this.namedSteps) {
                if (null == object) {
                    if (object == this.asMap.get(namedStep))
                        return true;
                } else if (object.equals(this.asMap.get(namedStep)))
                    return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return Arrays.asList(this.namedSteps).toString();
        }
    }
}
