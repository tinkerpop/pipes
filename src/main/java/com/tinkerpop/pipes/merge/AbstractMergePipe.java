package com.tinkerpop.pipes.merge;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.tinkerpop.pipes.Path;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractMergePipe<S> extends AbstractPipe<Iterator<S>, S> implements MergePipe<S> {

    protected Iterator<S> currentEnds;
    protected final List<Iterator<S>> allStarts = new ArrayList<Iterator<S>>();

    public AbstractMergePipe() {
        this.ensurePipeStarts = false;
    }

    public void setStarts(final Iterator<Iterator<S>> starts) {
        PipeHelper.fillCollection(starts, this.allStarts);
        this.starts = this.allStarts.iterator();
    }

    public void enablePath() {
        if (!this.pathEnabled) {
            this.pathEnabled = true;
            for (Iterator<S> start : this.allStarts) {
                if (start instanceof Path) {
                    Path path = (Path)start;
                    path.enablePath();
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
                ArrayList pathElements = path.path();
                return pathElements;
            } else {
                return new ArrayList();
            }
        } else {
            throw new NoSuchElementException("Path can not be returned until the iterator has been incremented.");
        }
    }

}
