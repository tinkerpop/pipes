package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.AbstractPipe;
import com.tinkerpop.pipes.serial.SingleIterator;

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

    public AndFilterPipe(final List<FilterPipe<S>> pipes) {
        this.pipes = pipes;
    }

    public S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            boolean and = true;
            for (FilterPipe<S> pipe : this.pipes) {
                pipe.setStarts(new SingleIterator<S>(s));
                if (!pipe.hasNext()) {
                    and = false;
                    break;
                } else {
                    pipe.clear();
                }
            }
            if (and)
                return s;
        }
        throw new NoSuchElementException();
    }

}