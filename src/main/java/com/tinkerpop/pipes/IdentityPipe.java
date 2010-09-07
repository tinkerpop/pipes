package com.tinkerpop.pipes;

import java.util.Iterator;

/**
 * The IdentityPipe is the most basic pipe.
 * It simply maps the input to the output without any processing.
 * <p/>
 * <pre>
 * protected S processNextStart() {
 *  return this.starts.next();
 * }
 * </pre>
 * <p/>
 * This Pipe is useful in various test case situations.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdentityPipe<S> extends AbstractPipe<S, S> {
    protected S processNextStart() {
        return this.starts.next();
    }

    public void setStarts(final Iterator<S> starts) {
        this.starts = starts;
    }

    public void setStarts(final Iterable<S> starts) {
        this.setStarts(starts.iterator());
    }
}
