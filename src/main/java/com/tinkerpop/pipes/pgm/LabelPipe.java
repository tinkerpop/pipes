package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LabelPipe extends AbstractPipe<Edge, String> {

	@Override
	public boolean hasNext() {
		return this.starts.hasNext();
	}

    protected String processNextStart() {
        return this.starts.next().getLabel();
    }
}
