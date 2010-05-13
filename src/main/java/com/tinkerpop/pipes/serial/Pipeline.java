package com.tinkerpop.pipes.serial;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A Pipeline is a linear composite of Pipes.
 * Pipeline takes a List of Pipes and joins them according to their order as specified by their location in the List.
 * It is important to ensure that the provided ordered Pipes can connect together.
 * That is, that the output of the n-1 Pipe is the same as the input to n Pipe.
 * Once all provided Pipes are composed, a Pipeline can be treated like any other Pipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class Pipeline<S, E> implements Pipe<S, E> {

    private Pipe<S, ?> startPipe;
    private Pipe<?, E> endPipe;


    public Pipeline() {
    }

    public Pipeline(final List<Pipe> pipes) {
        this.setPipes(pipes);
    }

    public Pipeline(final Pipe... pipes) {
        this(Arrays.asList(pipes));
    }

    public void setPipes(final List<Pipe> pipes) {
        this.startPipe = (Pipe<S, ?>) pipes.get(0);
        this.endPipe = (Pipe<?, E>) pipes.get(pipes.size() - 1);
        for (int i = 1; i < pipes.size(); i++) {
            pipes.get(i).setStarts((Iterator) pipes.get(i - 1));
        }
    }

    public void setStartPipe(final Pipe<S, ?> startPipe) {
        this.startPipe = startPipe;
    }

    public void setEndPipe(final Pipe<?, E> endPipe) {
        this.endPipe = endPipe;
    }

    public void setPipes(final Pipe... pipes) {
        this.setPipes(Arrays.asList(pipes));
    }

    public void setStarts(final Iterator<S> starts) {
        this.startPipe.setStarts(starts);
    }

    public void setStarts(final Iterable<S> starts) {
        this.setStarts(starts.iterator());
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return endPipe.hasNext();
    }

    public E next() {
        return endPipe.next();
    }

    public Iterator<E> iterator() {
        return this;
    }
}
