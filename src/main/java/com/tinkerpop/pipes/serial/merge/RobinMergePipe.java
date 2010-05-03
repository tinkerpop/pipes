package com.tinkerpop.pipes.serial.merge;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RobinMergePipe<S> extends AbstractMergePipe<S> {

    private List<Iterator<S>> allStarts = new ArrayList<Iterator<S>>();
    private int currentStarts = 0;

    public void setStarts(final Iterator<Iterator<S>> starts) {
        this.starts = starts;
        while (this.starts.hasNext()) {
            allStarts.add(this.starts.next());
        }

    }

    public void addStart(Iterator<S> start) {
        this.allStarts.add(start);
    }

    protected S processNextStart() {
        if (this.allStarts.size() > 0) {
            Iterator<S> starts = this.allStarts.get(this.currentStarts);
            if (starts.hasNext()) {
                this.currentStarts = ++this.currentStarts % this.allStarts.size();
                return starts.next();
            } else {
                this.allStarts.remove(this.currentStarts);
                if (this.allStarts.size() == 1)
                    this.currentStarts = 0;
                return this.processNextStart();
            }
        } else {
            throw new NoSuchElementException();
        }
    }
}