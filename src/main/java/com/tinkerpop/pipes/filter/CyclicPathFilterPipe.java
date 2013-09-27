package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * CyclicPathFilterPipe will only emit an object if its transformation path has no repeats (loops) in it.
 * This pipe requires that path calculations be enabled. As such, when the start is set, enablePath(true) is invoked.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CyclicPathFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private Set set = new LinkedHashSet();

    public void setStarts(Iterator<S> starts) {
        super.setStarts(starts);
        this.enablePath(true);
    }

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (this.starts instanceof Pipe) {
                final List path = ((Pipe) this.starts).getCurrentPath();
                this.set.clear();
                this.set.addAll(path);
                if (path.size() == this.set.size()) {
                    return s;
                }
            } else {
                return s;
            }
        }

    }
}
