package com.tinkerpop.pipes.transform;

import java.util.Collections;
import java.util.List;

/**
 * The ShufflePipe emits all the objects up to this step as an ArrayList then randomizes the order of the items
 * in the list.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class ShufflePipe<S> extends GatherPipe<S> {

    protected List<S> processNextStart() {
        final List<S> list = super.processNextStart();

        Collections.shuffle(list);

        return list;
    }
}
