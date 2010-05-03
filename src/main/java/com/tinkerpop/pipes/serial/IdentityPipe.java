package com.tinkerpop.pipes.serial;

/**
 * The IdentityPipe is the most basic Pipe.
 * It simply maps the input to the output without any processing.
 * This Pipe is useful in various test case situations.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdentityPipe<S> extends AbstractPipe<S, S> {
    protected S processNextStart() {
        return this.starts.next();
    }
}
