package com.tinkerpop.pipes.pgm;

import java.util.HashMap;
import java.util.Map;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PropertyMapPipe<S extends Element> extends AbstractPipe<S, Map<String, Object>> {
		@Override
		public boolean hasNext() {
    	return this.starts.hasNext();
		}
	
    protected Map<String, Object> processNextStart() {
        final S element = this.starts.next();
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final String key : element.getPropertyKeys()) {
            map.put(key, element.getProperty(key));
        }
        return map;
    }
}
