package com.tinkerpop.pipes.merge;

import com.tinkerpop.pipes.PipeHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import com.tinkerpop.pipes.Path;
import com.tinkerpop.pipes.Pipe;

/**
 * The RobinMergePipe will, in a round robin fashion, process an object from each pipe one in a row until all pipes are exhausted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RobinMergePipe<S> extends AbstractMergePipe<S> {

    private final List<Iterator<S>> allStarts = new ArrayList<Iterator<S>>();
    private int currentStarts = 0;

    public void setStarts(final Iterator<Iterator<S>> starts) {
        this.starts = starts;
        PipeHelper.fillCollection(this.starts, this.allStarts);
    }

    public ArrayList path() {
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

    protected S processNextStart() {
        if (this.allStarts.size() > 0) {
            Iterator<S> starts = this.allStarts.get(this.currentStarts);
            if (starts.hasNext()) {
                this.currentStarts = ++this.currentStarts % this.allStarts.size();
                this.currentEnds = starts;
                return starts.next();
            } else {
                this.allStarts.remove(this.currentStarts);
                if (this.allStarts.size() == this.currentStarts)
                    this.currentStarts = 0;
                return this.processNextStart();
            }
        } else {
            throw new NoSuchElementException();
        }
    }
}
