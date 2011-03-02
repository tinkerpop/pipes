package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdPipe extends AbstractPipe<Element, Object> {
		@Override
		public boolean hasNext() {
	    	return this.starts.hasNext();
		}
		
    protected Object processNextStart() {
        return this.starts.next().getId();
    }
}
