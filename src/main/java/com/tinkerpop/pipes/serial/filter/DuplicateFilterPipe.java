package com.tinkerpop.pipes.serial.filter;


import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * The DuplicateFilterPipe will not allow a duplicate object to pass through it.
 * This is accomplished by the Pipe maintaining an internal HashSet that is used to store a history of previously seen objects.
 * Thus, the more unique objects that pass through this Pipe, the slower it becomes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DuplicateFilterPipe<S> extends AbstractComparisonFilterPipe<S, S> {

    private final Set<S> objects = new HashSet<S>();

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            if (!this.doesContain(this.objects, s)) {
                this.objects.add(s);
                return s;
            }
        }
        throw new NoSuchElementException();
    }
}
