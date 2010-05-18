package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.Pipe;

/**
 * A ComparisonFilterPipe will allow or disallow objects that pass through it depending on some implemented test criteria.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface ComparisonFilterPipe<S,T> extends Pipe<S, S> {

    public enum Filter {
        ALLOW, DISALLOW
    }

    public boolean testObject(T object);

}
