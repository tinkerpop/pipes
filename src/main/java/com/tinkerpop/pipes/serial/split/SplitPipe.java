package com.tinkerpop.pipes.serial.split;


import com.tinkerpop.pipes.serial.Pipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface SplitPipe<S> extends Pipe<S, S> {

    public void addSplit();

    public Pipe<S, S> getSplit(int splitNumber);

    public void routeNext();

}
