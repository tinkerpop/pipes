package com.tinkerpop.pipes.merge;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        this.pathEnabled = true;
        for (Iterator<S> start : this.allStarts) {
            if (start instanceof Pipe) {
                Pipe pipe = (Pipe) start;
                pipe.enablePath();
            }
        }
    }

    public List getPath() {
        if (!this.pathEnabled) {
            throw new UnsupportedOperationException("To use path(), you must call enablePath() before iteration begins");
        }
        if (this.currentEnds != null) {
            if (this.currentEnds instanceof Pipe) {
                Pipe pipe = (Pipe) this.currentEnds;
                return pipe.getPath();
            } else {
                return new ArrayList();
            }
        } else {
            throw new NoSuchElementException("Path can not be returned until the iterator has been incremented");
        }
    }

}
