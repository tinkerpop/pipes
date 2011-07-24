package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import com.tinkerpop.pipes.branch.util.Bundle;
import com.tinkerpop.pipes.branch.util.ExpandableBundleIterator;
import com.tinkerpop.pipes.util.MetaPipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * LoopPipe takes a Boolean-based PipeClosure.
 * For each object of the LoopPipe, the PipeClosure is called.
 * The first parameter of the PipeClosure is a Bundle object which is the object plus some metadata.
 * The PipeClosure returns a Boolean.
 * The Boolean determines whether the object should be put back at the start of the LoopPipe or not.
 * In essence, the semantics of the PipeClosure is "while."
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopPipe<S> extends AbstractPipe<S, S> implements MetaPipe {

    private final PipeClosure<Boolean, Pipe> closure;
    private final Pipe<S, S> pipe;
    private ExpandableBundleIterator<S> expando;

    public LoopPipe(final Pipe<S, S> pipe, final PipeClosure<Boolean, Pipe> closure) {
        this.pipe = pipe;
        this.closure = closure;
        this.closure.setPipe(this);
    }

    protected S processNextStart() {
        while (true) {
            final S s = this.pipe.next();
            final Bundle<S> bundle = new Bundle<S>(s, this.getPath(), this.getLoops());
            if (closure.compute(bundle)) {
                this.expando.add(bundle);
            } else {
                return s;
            }
        }
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(pipe);
    }

    public void setStarts(final Iterator<S> iterator) {
        this.expando = new ExpandableBundleIterator<S>(iterator);
        this.pipe.setStarts(this.expando);
    }

    public String toString() {
        return super.toString() + "[" + this.pipe + "]";
    }

    public int getLoops() {
        return this.expando.getCurrentLoops() + 1;
    }

    public List getPath() {
        final List path = new ArrayList();
        final List currentPath = this.expando.getCurrentPath();
        if (null != currentPath)
            path.addAll(currentPath);
        path.addAll(this.pipe.getPath());
        return path;
    }

    public void reset() {
        this.expando.clear();
        this.pipe.reset();
        super.reset();
    }

}
