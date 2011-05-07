package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.sideeffect.AggregatorPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectCapPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectPipe;

import java.util.LinkedList;
import java.util.List;

/**
 * GatherPipe emits all the elements up to this step as a LinkedList.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipe<S> extends SideEffectCapPipe<S, List<S>> {

    public GatherPipe() {
        super((SideEffectPipe) new AggregatorPipe<S>(new LinkedList<S>()));
    }
}