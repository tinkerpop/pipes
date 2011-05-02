package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * The PropertyPipe returns the property value of the Element identified by the provided key.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PropertyPipe<S extends Element, E> extends AbstractPipe<S, E> {

    private final String key;
    private final boolean includeNull;

    public PropertyPipe(final String key) {
        this.key = key;
        this.includeNull = true;
    }

    public PropertyPipe(final String key, final boolean includeNull) {
        this.key = key;
        this.includeNull = includeNull;
    }

    protected E processNextStart() {
        while (true) {
            Element e = this.starts.next();
            try {
                E value = (E) e.getProperty(this.key);
                if (this.includeNull || value != null)
                    return value;
            } catch (Exception ex) {
                if (this.includeNull)
                    return null;
            }
        }
    }

    public String toString() {
        return super.toString() + "(" + this.key + ")";
    }
}
