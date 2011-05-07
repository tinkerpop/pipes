package com.tinkerpop.pipes.filter;


import com.tinkerpop.pipes.AbstractPipe;

import java.util.HashSet;

/**
 * The DuplicateFilterPipe will not allow a duplicate object to pass through it.
 * This is accomplished by the Pipe maintaining an internal HashSet that is used to store a history of previously seen objects.
 * Thus, the more unique objects that pass through this Pipe, the slower it becomes as a log_2 index is checked for every object.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DuplicateFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final HashSet<S> historySet = new HashSet<S>();

    protected S processNextStart() {
        while (true) {
            S s = this.starts.next();
            if (!this.historySet.contains(s)) {
                this.historySet.add(s);
                return s;
            }
        }
    }

    public void reset() {
        this.historySet.clear();
        super.reset();
    }
}
