package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.MetaPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;
import com.tinkerpop.pipes.ExpandableIterator;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

/**
 * FutureFilterPipe will allow an object to pass through it if the object has an output from the pipe provided in the constructor of the FutureFilterPipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FutureFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S>, MetaPipe {

    private final ExpandableIterator<S> expando;
    private final Pipe<S, ?> pipe;
    private final boolean pipeShouldHaveResult;

    public FutureFilterPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
        this.pipeShouldHaveResult = true;
        this.expando = new ExpandableIterator<S>(new ArrayList().iterator());
        pipe.setStarts(expando);
    }

    public FutureFilterPipe(final Pipe<S, ?> pipe, final boolean pipeShouldHaveResult) {
        this.pipe = pipe;
        this.pipeShouldHaveResult = pipeShouldHaveResult;
        this.expando = new ExpandableIterator<S>(new ArrayList().iterator());
        pipe.setStarts(expando);
    }

    public S processNextStart() {
        while (true) {
            S s = this.starts.next();
            this.expando.add(s);
            pipe.reset();
            if (pipe.hasNext()) {
                if (this.pipeShouldHaveResult) {
                    return s;
                }
            } else if (! this.pipeShouldHaveResult) {
                return s;
            }
        }
    }

    public String toString() {
        return super.toString() + "<" + this.pipe + ">";
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(this.pipe);
    }

}
