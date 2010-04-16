package com.tinkerpop.pipes.serial.filter;


import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DuplicateFilterPipe<S> extends AbstractFilterPipe<S,S> {

    private final Set<S> objects = new HashSet<S>();

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S tempEnd = this.starts.next();
            if (!this.doesContain(this.objects, tempEnd)) {
                this.objects.add(tempEnd);
                return tempEnd;
            }
        }
        throw new NoSuchElementException();
    }
}
