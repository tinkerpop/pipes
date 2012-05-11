package com.tinkerpop.pipes;

import java.util.Iterator;
import java.util.List;

/**
 * The generic interface for any Pipe implementation.
 * A Pipe takes/consumes objects of type S and returns/emits objects of type E.
 * S refers to <i>starts</i> and the E refers to <i>ends</i>.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @author Darrick Wiebe (darrick@innatesoftware.com)
 */
public interface Pipe<S, E> extends Iterator<E>, Iterable<E> {

    public static final String NO_PATH_MESSAGE = "Path calculations are not enabled";

    /**
     * Set an iterator of S objects to the head (start) of the pipe.
     *
     * @param starts the iterator of incoming objects
     */
    public void setStarts(Iterator<S> starts);

    /**
     * Set an iterable of S objects to the head (start) of the pipe.
     *
     * @param starts the iterable of incoming objects
     */
    public void setStarts(Iterable<S> starts);

    /**
     * Returns the transformation path to arrive at the current object of the pipe.
     *
     * @return a List of all of the objects traversed for the current iterator position of the pipe.
     */
    public List getCurrentPath();

    /**
     * Calculating paths can be an expensive operation for some pipes.
     * This method is used to activate or deactivate the calculation of paths.
     * The default state of a newly constructed pipe should have its path calculations disabled.
     * An implementation of this method should be recursive whereby the starts (if a Pipe) should have this method called on it.
     *
     * @param enable enable path calculations
     */
    public void enablePath(boolean enable);

    /**
     * A pipe may maintain state. Reset is used to remove state.
     * The general use case for reset() is to reuse a pipe in another computation without having to create a new Pipe object.
     * An implementation of this method should be recursive whereby the starts (if a Pipe) should have this method called on it.
     */
    public void reset();
}
