package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.Pipe;

/**
 * A ComparisonFilterPipe will allow or disallow objects that pass through it depending on some implemented comparison criteria.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface ComparisonFilterPipe<S,T> extends Pipe<S, S> {

    public enum Filter {
        EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL
    }

    public boolean compareObjectProperty(T objectProperty);

}
