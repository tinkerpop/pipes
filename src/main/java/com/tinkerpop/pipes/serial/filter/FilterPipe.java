package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.Pipe;

/**
 * A FilterPipe has no specified behavior save that it takes the same objects it emits.
 * This interface is used to allow one to specify that their Pipe will either emit the input or not.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface FilterPipe<S> extends Pipe<S, S> {
}
