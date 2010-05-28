package com.tinkerpop.pipes.serial.filter;


import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.HashSet;
import java.util.NoSuchElementException;

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
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            if (!this.historySet.contains(s)) {
                this.historySet.add(s);
                return s;
            }
        }
        throw new NoSuchElementException();
    }

    public void clearHistory() {
        this.historySet.clear();
    }
}
