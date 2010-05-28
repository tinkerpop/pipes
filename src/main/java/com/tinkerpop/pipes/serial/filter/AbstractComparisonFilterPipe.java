package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.AbstractPipe;

/**
 * The AbstractComparisonFilterPipe provides the necessary functionality that is required of most ComparisonFilterPipe implementations.
 * The compareObjectProperty() implementation compares the provided object with the internal object provided.
 * Depending on the type of ComparisonFilterPipe.Filter used, different types of comparisons are evaluated.
 * <pre>
 * public boolean compareObjectProperty(T objectProperty) {
 *    switch (this.filter) {
 *        case EQUALS:
 *            return this.storedObjectProperty.equals(objectProperty);
 *        case NOT_EQUALS:
 *            return !this.storedObjectProperty.equals(objectProperty);
 *        case GREATER_THAN:
 *            return ((Comparable) objectProperty).compareTo(this.storedObjectProperty) == 1;
 *        case LESS_THAN:
 *            return ((Comparable) objectProperty).compareTo(this.storedObjectProperty) == -1;
 *        case GREATER_THAN_EQUAL:
 *            return ((Comparable) objectProperty).compareTo(this.storedObjectProperty) >= 0;
 *        case LESS_THAN_EQUAL:
 *            return ((Comparable) objectProperty).compareTo(this.storedObjectProperty) <= 0;
 *        default:
 *            return false;
 *    }
 * }
 * </pre>
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractComparisonFilterPipe<S, T> extends AbstractPipe<S, S> implements ComparisonFilterPipe<S, T> {

    protected final T storedObjectProperty;
    protected final Filter filter;

    public AbstractComparisonFilterPipe(final T storedObjectProperty, final Filter filter) {
        this.storedObjectProperty = storedObjectProperty;
        this.filter = filter;
    }

    public boolean compareObjectProperty(final T objectProperty) {
        switch (this.filter) {
            case EQUALS:
                return this.storedObjectProperty.equals(objectProperty);
            case NOT_EQUALS:
                return !this.storedObjectProperty.equals(objectProperty);
            case GREATER_THAN:
                return ((Comparable) objectProperty).compareTo(this.storedObjectProperty) == 1;
            case LESS_THAN:
                return ((Comparable) objectProperty).compareTo(this.storedObjectProperty) == -1;
            case GREATER_THAN_EQUAL:
                return ((Comparable) objectProperty).compareTo(this.storedObjectProperty) >= 0;
            case LESS_THAN_EQUAL:
                return ((Comparable) objectProperty).compareTo(this.storedObjectProperty) <= 0;
            default:
                return false;
        }
    }
}
