package com.tinkerpop.pipes.serial;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdentityPipe<S> extends AbstractPipe<S, S> {
    protected S processNextStart() {
        return this.starts.next();
    }
}
