package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.FastNoSuchElementException;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The ShufflePipe emits all the objects up to this step as an ArrayList then randomizes the order of the items
 * in the list.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class ShufflePipe<S> extends AbstractPipe<S, S> implements TransformPipe<S, S> {
    private Iterator<S> tempIterator = PipeHelper.emptyIterator();

    private List<List> listPaths = new ArrayList<List>();


    public List getCurrentPath() {
        if (pathEnabled)
            return new ArrayList(this.listPaths);
        else
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
    }

    protected S processNextStart() {
        while (true) {
            if (tempIterator.hasNext()) {
                return this.tempIterator.next();
            } else {
                final List<S> list = gather();
                Collections.shuffle(list);
                this.tempIterator = list.iterator();
            }
        }

    }

    protected List<S> gather() {
        final List<S> list = new ArrayList<S>();
        this.listPaths = new ArrayList<List>();
        if (!this.starts.hasNext()) {
            throw FastNoSuchElementException.instance();
        } else {
            while (this.starts.hasNext()) {
                final S s = this.starts.next();
                list.add(s);
                if (this.pathEnabled)
                    this.listPaths.add(super.getPathToHere());
            }
        }

        if (this.pathEnabled) {
            return addList(list);
        } else {
            return list;
        }
    }

    public void reset() {
        this.listPaths = new ArrayList<List>();
        super.reset();
    }

    private List addList(final List list) {
        for (final List l : this.listPaths) {
            l.add(list);
        }
        return list;
    }
}
