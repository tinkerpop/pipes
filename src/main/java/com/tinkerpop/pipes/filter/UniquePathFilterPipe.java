package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class UniquePathFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            if (this.starts instanceof Pipe) {
                final List path = ((Pipe) this.starts).getPath();
                final Set<Object> temp = new HashSet<Object>();
                boolean doReturn = true;
                for (final Object object : path) {
                    if (temp.contains(object)) {
                        doReturn = false;
                        break;
                    } else {
                        temp.add(object);
                    }
                }
                if (doReturn)
                    return s;
            } else {
                return s;
            }
        }

    }
}
