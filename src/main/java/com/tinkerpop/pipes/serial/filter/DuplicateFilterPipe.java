package com.tinkerpop.pipes.serial.filter;


import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * The DuplicateFilterPipe will not allow a duplicate object to pass through it.
 * This is accomplished by the Pipe maintaining an internal HashSet that is used to store a history of previously seen objects.
 * Thus, the more unique objects that pass through this Pipe, the slower it becomes as a log_2 index is checked for every object.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DuplicateFilterPipe<S> extends AbstractComparisonFilterPipe<S,S> {

    public DuplicateFilterPipe() {
        super(new HashSet<S>(), Filter.DISALLOW);
    }

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            if (this.testObject(s)) {
                this.storedCollection.add(s);
                return s;
            }
        }
        throw new NoSuchElementException();
    }
}
