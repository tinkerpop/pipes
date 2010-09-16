package com.tinkerpop.pipes.merge;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.ArrayList;
import java.util.Iterator;
import com.tinkerpop.pipes.Path;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractMergePipe<S> extends AbstractPipe<Iterator<S>, S> implements MergePipe<S> {

    protected Iterator<S> currentEnds;

    public AbstractMergePipe() {
        this.ensurePipeStarts = false;
    }

    public void enablePath() {
        if (!this.pathEnabled) {
            this.pathEnabled = true;
            for (Iterator<S> start : this.starts) {
                if (start instanceof Path) {
                    Path path = (Path)start;
                    start.enablePath();
                }
            }
        }
    }

    public ArrayList path() {
        if (!this.pathEnabled) {
            throw new UnsupportedOperationException("To use path(), you must call enablePath() before iteration begins.");
        }
        if (this.currentEnds != null) {
            if (this.currentEnds instanceof Path) {
                Path path = (Path)this.currentEnds;
                return path.path();
            } else {
                return new ArrayList();
            }
        } else {
            throw new NoSuchElementException("Path can not be returned until the iterator has been incremented.");
        }
    }

}
