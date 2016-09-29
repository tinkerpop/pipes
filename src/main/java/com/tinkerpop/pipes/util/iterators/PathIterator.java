package com.tinkerpop.pipes.util.iterators;

import java.util.Iterator;
import java.util.List;

/**
 * An iterator that also records the path which has led to the current point.
 */
public interface PathIterator<T> extends Iterator<T> {
    List getCurrentPath();
}
