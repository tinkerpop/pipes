package com.tinkerpop.pipes.filter;


import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The DuplicateFilterPipe will not allow a duplicate object to pass through it.
 * This is accomplished by the Pipe maintaining an internal HashSet that is used to store a history of previously seen objects.
 * Thus, the more unique objects that pass through this Pipe, the slower it becomes as a log_2 index is checked for every object.
 * Also, beware of OutOfMemoryExceptions as if the number of distinct objects is too great, then the HashSet will overflow memory.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DuplicateFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final Set historySet = new LinkedHashSet();
    private final PipeFunction<S, ?> function;

    public DuplicateFilterPipe() {
        this.function = null;
    }

    public DuplicateFilterPipe(final PipeFunction<S, ?> function) {
        this.function = function;
    }


    protected S processNextStart() {
        while (true) {
            final S s = this.starts.next();

            Object t;
            if (null != this.function) {
                t = this.function.compute(s);
            } else {
                t = s;
            }

            if (this.historySet.add(t)) {
                return s;
            }
        }
    }

    public void reset() {
        this.historySet.clear();
        super.reset();
    }
}
