package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The OrFilterPipe takes a collection of FilterPipes. Each FilterPipe is fed the same incoming object.
 * If one of the FilterPipes emit the object, then the OrFilterPipe emits the object. If not, then the incoming object is not emitted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final List<FilterPipe<S>> pipes;


    public OrFilterPipe(final FilterPipe<S>... pipes) {
        this.pipes = Arrays.asList(pipes);
    }

    public OrFilterPipe(final List<FilterPipe<S>> pipes) {
        this.pipes = pipes;
    }

    public S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            for (FilterPipe<S> pipe : this.pipes) {
                pipe.setStarts(Arrays.asList(s));
                if (pipe.hasNext()) {
                    pipe.clear();
                    return s;
                }
            }
        }
        throw new NoSuchElementException();
    }

}
