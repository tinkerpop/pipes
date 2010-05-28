package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.AbstractPipe;
import com.tinkerpop.pipes.serial.Pipe;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The AndFilterPipe takes a collection of FilterPipes. Each FilterPipe is fed the same incoming object.
 * If all the FilterPipes emit the object, then the AndFilterPipe emits the object. If not, then the incoming object is not emitted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AndFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final List<FilterPipe<S>> pipes;

    public AndFilterPipe(final FilterPipe<S>... pipes) {
        this.pipes = Arrays.asList(pipes);
    }

    public S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            S e = null;
            boolean and = true;
            for (Pipe<S, S> pipe : this.pipes) {
                pipe.setStarts(Arrays.asList(s));
                if (pipe.hasNext()) {
                    e = pipe.next();
                } else {
                    and = false;
                    break;
                }
            }
            if (and && null != e)
                return e;
        }
        throw new NoSuchElementException();
    }

}