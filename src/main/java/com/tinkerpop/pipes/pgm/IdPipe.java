package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdPipe extends AbstractPipe<Element, Object> {

    protected Object processNextStart() {
        return this.starts.next().getId();
    }
}
