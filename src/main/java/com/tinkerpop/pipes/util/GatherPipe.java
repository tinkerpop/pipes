package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.sideeffect.AggregatorPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectCapPipe;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipe<S> extends AbstractPipe<S, List<S>> {

    private final Pipe pipe = new SideEffectCapPipe<S, Collection<S>>(new AggregatorPipe<S>(new LinkedList<S>()));

    public void setStarts(Iterable starts) {
        this.pipe.setStarts(starts);
    }

    public void setStarts(Iterator starts) {
        this.pipe.setStarts(starts);
    }

    public List<S> processNextStart() {
        return (List<S>) this.pipe.next();
    }
}