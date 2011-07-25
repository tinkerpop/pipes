package com.tinkerpop.pipes.filter;


import com.tinkerpop.pipes.AbstractPipe;

/**
 * The AbstractComparisonFilterPipe provides the necessary functionality that is required of most ComparisonFilterPipe implementations.
 * The compareObjects() implementation is useful for comparing two objects to determine if the current object in the pipe should be filtered.
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

    protected final Filter filter;

    public AbstractComparisonFilterPipe(final Filter filter) {
        this.filter = filter;
    }

    public boolean compareObjects(final T leftObject, final T rightObject) {
        switch (this.filter) {
            case EQUAL:
                if (null == leftObject)
                    return rightObject == null;
                return leftObject.equals(rightObject);
            case NOT_EQUAL:
                if (null == leftObject)
                    return rightObject != null;
                return !leftObject.equals(rightObject);
            case GREATER_THAN:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) == 1;
            case LESS_THAN:
                if (null == leftObject || rightObject == null)
                    return false;
                return ((Comparable) leftObject).compareTo(rightObject) == -1;
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
