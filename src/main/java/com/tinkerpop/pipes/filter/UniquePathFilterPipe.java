package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;

import java.util.HashSet;
import java.util.List;

/**
 * UniquePathFilterPipe will only emit an object if its transformation path has no repeats (loops) in it.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class UniquePathFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (this.starts instanceof Pipe) {
                final List path = ((Pipe) this.starts).getPath();
                if (path.size() == new HashSet(path).size()) {
                    return s;
                }
            } else {
                return s;
            }
        }

    }
}
