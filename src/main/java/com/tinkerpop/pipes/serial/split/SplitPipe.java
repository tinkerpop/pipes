package com.tinkerpop.pipes.serial.split;


import com.tinkerpop.pipes.serial.Pipe;

/**
 * A SplitPipe has a single input, but one or more outputs.
 * Each output is called a split. Each split is a Pipe. Each split is identified by a unique integer number.
 * The method routeNext() determines how each next() start is routed to the splits.
 * The method getSplit() is used to get each split so that it can be attached to the next pipe in the chain. 
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface SplitPipe<S> extends Pipe<S, S> {

    public void addSplit();

    public Pipe<S, S> getSplit(int splitNumber);

    public void routeNext();

}
