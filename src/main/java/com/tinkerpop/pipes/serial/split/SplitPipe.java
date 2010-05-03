package com.tinkerpop.pipes.serial.split;


import com.tinkerpop.pipes.serial.Pipe;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface SplitPipe<S> extends Pipe<S, S> {

    public Iterator<S> getSplit(int splitNumber);

    public void fillNext(int splitNumber);

    public void addSplit();

}
