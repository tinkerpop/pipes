package com.tinkerpop.pipes.serial.merge;

import com.tinkerpop.pipes.serial.Pipe;

import java.util.Iterator;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface MergePipe<S> extends Pipe<Iterator<S>,S> {

    //public void addStart(Iterator<S> start);
}
