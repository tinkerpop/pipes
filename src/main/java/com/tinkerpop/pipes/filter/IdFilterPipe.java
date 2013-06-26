package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Compare;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Query;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdFilterPipe extends AbstractPipe<Element, Element> implements FilterPipe<Element> {

    private final Object[] ids;
    private final Compare filter;

    public IdFilterPipe(final Compare filter, final Object... ids) {
        this.ids = ids;
        this.filter = filter;
    }

    protected Element processNextStart() {
        while (true) {
            final Element element = this.starts.next();
            if (PipeHelper.compareObjectArray(this.filter, element.getId(), this.ids)) {
                return element;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.filter, Arrays.asList(this.ids));
    }
}
