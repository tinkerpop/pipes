package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Contains;
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
    private final Contains contains;

    public CollectionFilterPipe(final Collection<S> storedCollection, final Contains contains) {
        this.storedCollection = storedCollection;
        this.contains = contains;
    }

    public CollectionFilterPipe(final Contains contains, final AsMap asMap, final String... namedSteps) {
        this.storedCollection = new DynamicList<S>(asMap, namedSteps);
        this.contains = contains;
    }

    protected S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (this.contains.evaluate(s, storedCollection)) {
                return s;
            }
        }
    }

    public String toString() {
        if (this.storedCollection instanceof DynamicList)
            return PipeHelper.makePipeString(this, this.contains, ((DynamicList) this.storedCollection).toString());
        else
            return PipeHelper.makePipeString(this, this.contains);
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
