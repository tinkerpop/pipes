package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.transform.HasNextPipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * The AndFilterPipe takes a collection of pipes. Each provided pipe is wrapped in a HasNextPipe and is fed the same incoming S object.
 * If all the pipes emit true, then the AndFilterPipe emits the incoming S object. If not, then the incoming S object is not emitted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AndFilterPipe<S> extends AbstractMetaPipe<S, S> implements FilterPipe<S>, MetaPipe {

    private final List<Pipe<S, Boolean>> pipes = new ArrayList<Pipe<S, Boolean>>();

    public AndFilterPipe(final Pipe<S, ?>... pipes) {
        for (final Pipe<S, ?> pipe : pipes) {
            this.pipes.add(new HasNextPipe<S>(pipe));
        }
    }

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            boolean and = true;
            for (final Pipe<S, Boolean> pipe : this.pipes) {
                pipe.setStarts(new SingleIterator<S>(s));
                if (!pipe.next()) {
                    and = false;
                    break;
                }
            }
            if (and)
                return s;
        }
    }

    public List<Pipe> getPipes() {
        return (List) this.pipes;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }

}