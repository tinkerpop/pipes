package com.tinkerpop.pipes.merge;

import com.tinkerpop.pipes.Pipe;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface MergePipe<S> extends Pipe<Iterator<S>, S> {

    //public void addStart(Iterator<S> start);
}
