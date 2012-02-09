package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FluentUtility {

    /**
     * Get all the AsPipes in the provided MetaPipe.
     *
     * @return all the AsPipes
     */
    public static List<AsPipe> getAsPipes(final MetaPipe metaPipe) {
        final List<AsPipe> asPipes = new ArrayList<AsPipe>();
        for (final Pipe subPipe : metaPipe.getPipes()) {
            if (subPipe instanceof AsPipe) {
                asPipes.add((AsPipe) subPipe);
            }
            if (subPipe instanceof MetaPipe) {
                asPipes.addAll(FluentUtility.getAsPipes((MetaPipe) subPipe));
            }
        }
        return asPipes;
    }

    /**
     * A utility method to remove all the pipes back some number of steps and return them as a list.
     *
     * @param numberedStep the number of steps back to remove from the pipeline
     * @return the removed pipes
     */
    public static List<Pipe> removePreviousPipes(final Pipeline pipeline, final int numberedStep) {
        final List<Pipe> previousPipes = new ArrayList<Pipe>();
        for (int i = pipeline.size() - 1; i > ((pipeline.size() - 1) - numberedStep); i--) {
            previousPipes.add(0, pipeline.get(i));
        }
        for (int i = 0; i < numberedStep; i++) {
            pipeline.remove(pipeline.size() - 1);
        }

        if (pipeline.size() == 1)
            pipeline.setStarts(pipeline.getStarts());

        return previousPipes;
    }

    /**
     * A utility method to remove all the pipes back some partition step and return them as a list.
     *
     * @param namedStep the name of the step previous to remove from the pipeline
     * @return the removed pipes
     */
    public static List<Pipe> removePreviousPipes(final Pipeline pipeline, final String namedStep) {
        final List<Pipe> previousPipes = new ArrayList<Pipe>();
        for (int i = pipeline.size() - 1; i >= 0; i--) {
            final Pipe pipe = pipeline.get(i);
            if (pipe instanceof AsPipe && ((AsPipe) pipe).getName().equals(namedStep)) {
                break;
            } else {
                previousPipes.add(0, pipe);
            }
        }
        for (int i = 0; i < previousPipes.size(); i++) {
            pipeline.remove(pipeline.size() - 1);
        }

        if (pipeline.size() == 1)
            pipeline.setStarts(pipeline.getStarts());

        return previousPipes;
    }

    public static void setStarts(final Pipeline pipeline, final Object starts) {
        if (starts instanceof Iterator) {
            pipeline.setStarts((Iterator) starts);
        } else if (starts instanceof Iterable) {
            pipeline.setStarts(((Iterable) starts).iterator());
        } else {
            pipeline.setStarts(new SingleIterator(starts));
        }
    }
}
