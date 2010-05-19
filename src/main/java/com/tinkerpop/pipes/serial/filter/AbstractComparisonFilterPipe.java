package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Collection;

/**
 * The AbstractComparisonFilterPipe provides the necessary functionality that is required of most ComparisonFilterPipe implementations.
 * The testObjectProperty() implementation compares the provided object with the internal object or collection depending on the store object type.
 * <pre>
 *  public boolean testObjectProperty(T objectProperty) {
 *       if (null != this.storedObjectProperty) {
 *           if (this.filter == Filter.ALLOW)
 *               return this.storedObjectProperty.equals(object);
 *           else
 *               return !this.storedObjectProperty.equals(object);
 *       } else {
 *           if (this.filter == Filter.ALLOW)
 *               return this.storedPropertyCollection.contains(object);
 *           else
 *               return !this.storedPropertyCollection.contains(object);
 *       }
 *   }
 * </pre>
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractComparisonFilterPipe<S, T> extends AbstractPipe<S, S> implements ComparisonFilterPipe<S, T> {

    protected final T storedObjectProperty;
    protected final Collection<T> storedPropertyCollection;
    protected final Filter filter;

    public AbstractComparisonFilterPipe(T storedObjectProperty, Filter filter) {
        this.storedObjectProperty = storedObjectProperty;
        this.storedPropertyCollection = null;
        this.filter = filter;
    }

    public AbstractComparisonFilterPipe(Collection<T> storedPropertyCollection, Filter filter) {
        this.storedObjectProperty = null;
        this.storedPropertyCollection = storedPropertyCollection;
        this.filter = filter;
    }

    public boolean testObjectProperty(T objectProperty) {
        if (null != this.storedObjectProperty) {
            if (this.filter == Filter.ALLOW)
                return this.storedObjectProperty.equals(objectProperty);
            else
                return !this.storedObjectProperty.equals(objectProperty);
        } else {
            if (this.filter == Filter.ALLOW)
                return this.storedPropertyCollection.contains(objectProperty);
            else
                return !this.storedPropertyCollection.contains(objectProperty);
        }
    }
}
