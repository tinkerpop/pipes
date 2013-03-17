package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.iterators.SingleExpandableIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class MemoizePipe<S, E> extends AbstractMetaPipe<S, E> implements MetaPipe, TransformPipe<S, E> {

    protected Pipe<S, E> pipe;
    protected Map<S, List<E>> map;
    protected Iterator<E> currentIterator = PipeHelper.emptyIterator();
    protected final SingleExpandableIterator<S> expando = new SingleExpandableIterator<S>();

    public MemoizePipe(final Pipe<S, E> pipe) {
        this(pipe, new HashMap<S, List<E>>());
    }

    public MemoizePipe(final Pipe<S, E> pipe, final Map<S, List<E>> map) {
        this.pipe = pipe;
        this.pipe.setStarts(this.expando);
        this.map = map;
    }

    public E processNextStart() {
        while (true) {
            if (this.currentIterator.hasNext())
                return this.currentIterator.next();
            else {
                this.getOrCreate(this.starts.next());
            }
        }
    }

    private void getOrCreate(final S s) {
        if (this.map.containsKey(s)) {
            this.currentIterator = this.map.get(s).iterator();
        } else {
            this.expando.add(s);
            final List<E> results = new ArrayList<E>();
            PipeHelper.fillCollection(this.pipe, results);
            this.map.put(s, results);
            this.currentIterator = results.iterator();
        }
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(pipe);
    }

    public void reset() {
        this.currentIterator = PipeHelper.emptyIterator();
        try {
            this.map = this.map.getClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        super.reset();
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipe);
    }
}
