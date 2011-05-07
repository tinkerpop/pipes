package com.tinkerpop.pipes;

import java.util.List;

/**
 * A MetaPipe is a pipe that "wraps" some collection of pipes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface MetaPipe {

    public List<Pipe> getPipes();
}
