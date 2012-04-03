package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * CyclicPathFilterPipe will only emit an object if its transformation path has no repeats (loops) in it.
 * This pipe requires that path calculations be enabled. As such, when the start is set, enablePath(true) is invoked.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CyclicPathFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    public void setStarts(Iterator<S> starts) {
        super.setStarts(starts);
        this.enablePath(true);
    }

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (this.starts instanceof Pipe) {
                final List path = ((Pipe) this.starts).getCurrentPath();
                if (path.size() == new HashSet(path).size()) {
                    return s;
                }
            } else {
                return s;
            }
        }

    }
}
