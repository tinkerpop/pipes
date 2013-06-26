package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.CompareRelation;
import com.tinkerpop.blueprints.Contains;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdFilterPipe extends AbstractPipe<Element, Element> implements FilterPipe<Element> {

    private final Object id;
    private final CompareRelation compareRelation;

    public IdFilterPipe(final CompareRelation compareRelation, final Object id) {
        this.id = id;
        this.compareRelation = compareRelation;
    }

    protected Element processNextStart() {
        while (true) {
            final Element s = this.starts.next();
            if (this.compareRelation.compare(s.getId(), this.id))
                return s;
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.compareRelation, this.id);
    }
}
