package com.tinkerpop.pipes;

import java.util.Iterator;

public interface EmittingPipe<E> extends Iterator<E>, Iterable<E> {

    public void finishIteration();	
    	
}
