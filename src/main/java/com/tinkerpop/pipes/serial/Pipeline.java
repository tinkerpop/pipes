package com.tinkerpop.pipes.serial;

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

    public Pipeline(final List<Pipe> pipes) {
        this.setPipes(pipes);
    }

    public Pipeline() {
    }

    public void setPipes(final List<Pipe> pipes) {
        this.startPipe = pipes.get(0);
        this.endPipe = pipes.get(pipes.size() - 1);
        for (int i = 1; i < pipes.size(); i++) {
            pipes.get(i).setStarts((Iterator) pipes.get(i - 1));
        }
    }

    public void setStarts(final Iterator<S> starts) {
        startPipe.setStarts(starts);
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
