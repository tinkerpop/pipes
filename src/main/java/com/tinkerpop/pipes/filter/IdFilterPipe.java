package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Predicate;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdFilterPipe extends AbstractPipe<Element, Element> implements FilterPipe<Element> {

    private final Object id;
    private final Predicate predicate;

    public IdFilterPipe(final Predicate predicate, final Object id) {
        this.id = id;
        this.predicate = predicate;
    }

    protected Element processNextStart() {
        while (true) {
            final Element s = this.starts.next();
            if (this.predicate.evaluate(s.getId(), this.id))
                return s;
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.predicate, this.id);
    }
}
