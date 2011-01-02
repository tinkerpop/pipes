package com.tinkerpop.pipes;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BaseIdentityPipe<S> extends AbstractPipe<S, S> {
    protected S processNextStart() {
        return this.starts.next();
    }

    public void setStarts(final Iterator<S> starts) {
        this.ensurePipeStarts = false;
        this.starts = starts;
    }
}