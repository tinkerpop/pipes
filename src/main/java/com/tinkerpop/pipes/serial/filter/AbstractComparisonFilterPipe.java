package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Collection;

/**
 * The AbstractComparisonFilterPipe provides the necessary functionality that is required of most ComparisonFilterPipe implementations.
 * The testObject() implementation compares the provided object with the internal object or collection depending on the store object type.
 * <pre>
 *  public boolean testObject(T object) {
 *       if (null != this.storedObject) {
 *           if (this.filter == Filter.ALLOW)
 *               return this.storedObject.equals(object);
 *           else
 *               return !this.storedObject.equals(object);
 *       } else {
 *           if (this.filter == Filter.ALLOW)
 *               return this.storedCollection.contains(object);
 *           else
 *               return !this.storedCollection.contains(object);
 *       }
 *   }
 * </pre>
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractComparisonFilterPipe<S, T> extends AbstractPipe<S, S> implements ComparisonFilterPipe<S, T> {

    protected final T storedObject;
    protected final Collection<T> storedCollection;
    protected final Filter filter;

    public AbstractComparisonFilterPipe(T storedObject, Filter filter) {
        this.storedObject = storedObject;
        this.storedCollection = null;
        this.filter = filter;
    }

    public AbstractComparisonFilterPipe(Collection<T> storedCollection, Filter filter) {
        this.storedObject = null;
        this.storedCollection = storedCollection;
        this.filter = filter;
    }

    public boolean testObject(T object) {
        if (null != this.storedObject) {
            if (this.filter == Filter.ALLOW)
                return this.storedObject.equals(object);
            else
                return !this.storedObject.equals(object);
        } else {
            if (this.filter == Filter.ALLOW)
                return this.storedCollection.contains(object);
            else
                return !this.storedCollection.contains(object);
        }
    }
}
