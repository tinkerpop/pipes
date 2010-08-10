package com.tinkerpop.pipes.filter;


import com.tinkerpop.pipes.AbstractPipe;

/**
 * The AbstractComparisonFilterPipe provides the necessary functionality that is required of most ComparisonFilterPipe implementations.
 * The compareObjectProperty() implementation compares the provided object with the internal object provided.
 * The compareObjects() implementation is useful for comparing two objects that are not necessarily stored in the pipe.
 * Depending on the type of ComparisonFilterPipe.Filter used, different types of comparisons are evaluated.
 * <p/>
 * <pre>
 *   public boolean compareObjects(final T leftObject, final T rightObject) {
 *       switch (this.filter) {
 *           case EQUAL:
 *               if (null == leftObject)
 *                   return rightObject != null;
 *               return !leftObject.equals(rightObject);
 *           case NOT_EQUAL:
 *               if (null == leftObject)
 *                   return rightObject == null;
 *               return leftObject.equals(rightObject);
 *           case GREATER_THAN:
 *               if (null == leftObject || rightObject == null)
 *                   return false;
 *               return ((Comparable) leftObject).compareTo(rightObject) > 0;
 *           case LESS_THAN:
 *               if (null == leftObject || rightObject == null)
 *                   return false;
 *               return ((Comparable) leftObject).compareTo(rightObject) < 0;
 *           case GREATER_THAN_EQUAL:
 *               if (null == leftObject || rightObject == null)
 *                   return false;
 *               return ((Comparable) leftObject).compareTo(rightObject) >= 0;
 *           case LESS_THAN_EQUAL:
 *               if (null == leftObject || rightObject == null)
 *                   return false;
 *               return ((Comparable) leftObject).compareTo(rightObject) <= 0;
 *           default:
 *               throw new RuntimeException("Invalid state as no valid filter was provided");
 *       }
 *   }
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

    public AbstractComparisonFilterPipe(final Filter filter) {
        this(null, filter);
    }

    public boolean compareObjectProperty(final T objectProperty) {
        return this.compareObjects(this.storedObjectProperty, objectProperty);
    }

    public boolean compareObjects(final T leftObject, final T rightObject) {
        switch (this.filter) {
            case EQUAL:
                if (null == leftObject)
                    return rightObject != null;
                return !leftObject.equals(rightObject);
            case NOT_EQUAL:
                if (null == leftObject)
                    return rightObject == null;
                return leftObject.equals(rightObject);
            case GREATER_THAN:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) > 0;
            case LESS_THAN:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) < 0;
            case GREATER_THAN_EQUAL:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) >= 0;
            case LESS_THAN_EQUAL:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) <= 0;
            default:
                throw new RuntimeException("Invalid state as no valid filter was provided");
        }
    }
}
