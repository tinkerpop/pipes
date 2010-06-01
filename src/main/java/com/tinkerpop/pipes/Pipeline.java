package com.tinkerpop.pipes;

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

    /**
     * Constructs a pipeline from the provided pipes. The ordered list determines how the pipes will be chained together.
     * When the pipes are chained together, the start of pipe n is the end of pipe n-1.
     *
     * @param pipes the ordered list of pipes to chain together into a pipeline
     */
    public Pipeline(final List<Pipe> pipes) {
        this.setPipes(pipes);
    }


    /**
     * Constructs a pipeline from the provided pipes. The ordered array determines how the pipes will be chained together.
     * When the pipes are chained together, the start of pipe n is the end of pipe n-1.
     *
     * @param pipes the ordered array of pipes to chain together into a pipeline
     */
    public Pipeline(final Pipe... pipes) {
        this(Arrays.asList(pipes));
    }

    /**
     * Use when extending Pipeline and setting the pipeline chain without making use of the constructor.
     *
     * @param pipes the ordered list of pipes to chain together into a pipeline
     */
    protected void setPipes(final List<Pipe> pipes) {
        this.startPipe = (Pipe<S, ?>) pipes.get(0);
        this.endPipe = (Pipe<?, E>) pipes.get(pipes.size() - 1);
        for (int i = 1; i < pipes.size(); i++) {
            pipes.get(i).setStarts((Iterator) pipes.get(i - 1));
        }
    }

    /**
     * Use when extending Pipeline and setting the pipeline chain without making use of the constructor.
     *
     * @param pipes the ordered array of pipes to chain together into a pipeline
     */
    protected void setPipes(final Pipe... pipes) {
        this.setPipes(Arrays.asList(pipes));
    }

    /**
     * Only use if the intermediate pipes of the pipeline have been chained together manually.
     *
     * @param startPipe the start of the pipeline
     */
    public void setStartPipe(final Pipe<S, ?> startPipe) {
        this.startPipe = startPipe;
    }

    /**
     * Only use if the intermediate pipes of the pipeline have been chained together manually.
     *
     * @param endPipe the end of the pipeline
     */
    public void setEndPipe(final Pipe<?, E> endPipe) {
        this.endPipe = endPipe;
    }

    public void setStarts(final Iterator<S> starts) {
        this.startPipe.setStarts(starts);
    }

    public void setStarts(final Iterable<S> starts) {
        this.setStarts(starts.iterator());
    }

    /**
     * An unsupported operation that throws an UnsupportedOperationException.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines if there is another object that can be emitted from the pipeline.
     *
     * @return true if an object can be next()'d out of the pipeline
     */
    public boolean hasNext() {
        return endPipe.hasNext();
    }

    /**
     * Get the next object emitted from the pipeline.
     * If no such object exists, then a NoSuchElementException is thrown.
     *
     * @return the next emitted object
     */
    public E next() {
        return endPipe.next();
    }

    /**
     * Simply returns this as as a pipeline (more specifically, pipe) implements Iterator.
     *
     * @return returns the iterator representation of this pipeline
     */
    public Iterator<E> iterator() {
        return this;
    }
}
