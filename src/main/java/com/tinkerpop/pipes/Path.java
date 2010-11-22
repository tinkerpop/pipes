package com.tinkerpop.pipes;
import java.util.ArrayList;

/**
 * This untyped interface will return a collection of the various
 * objects that were used to traverse from the starting point to the
 * ultimate value of the pipe for a given iteration. The objects will
 * be of various types, for example when retriving the path from a graph
 * iteration, the path will consist of alternating edges and vertices.
 *
 * This interface is meant to be included in Pipe<S, E> and as such is
 * designed to return the path for the current iteration position of its
 * pipe.
 *
 * @author Darrick Wiebe (darrick@innatesoftware.com)
 */
public interface Path {
    /**
     * Turns on the additional caching and logic that is needed
     * to calculate paths. Enabling paths requires more memory and 
     * processing, so will be slightly slower.
     */
    public void enablePath();

    /**
     * Returns the path traversed to arrive at the current result of
     * the pipe.
     *
     * @return an ArrayList of all of the objects of various types
     * traversed for the current iterator position of the pipe.
     */
    public ArrayList getPath();
}
