package com.tinkerpop.pipes;

import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractMetaPipe<S, E> extends AbstractPipe<S, E> {
    public void enablePath() {
        super.enablePath();
        this.getInternalPipe().enablePath();
    }

    public List getPath() {
        if (!this.pathEnabled) {
            throw new UnsupportedOperationException("To use getPath(), you must call enablePath() before iteration begins");
        }
        List pathElements = this.getInternalPipe().getPath();
        int size = pathElements.size();
        /*if (size == 0 || pathElements.get(size - 1) != getCurrentEnd()) {
            pathElements.addAll(this.getInternalPipe().getPath());
        }*/
        return pathElements;
    }

    public abstract Pipe<S, ?> getInternalPipe();

}
