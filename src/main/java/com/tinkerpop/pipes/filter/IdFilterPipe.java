package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdFilterPipe extends AbstractPipe<Element, Element> implements FilterPipe<Element> {

    private final Object id;
    private final FilterPipe.Filter filter;

    public IdFilterPipe(final Object id, final FilterPipe.Filter filter) {
        this.id = id;
        this.filter = filter;
    }

    protected Element processNextStart() {
        while (true) {
            Element element = this.starts.next();
            if (PipeHelper.compareObjects(this.filter, element.getId(), this.id)) {
                return element;
            }
        }
    }

    public String toString() {
        return super.toString() + "(" + this.filter + "," + this.id + ")";
    }
}
