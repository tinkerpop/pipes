package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.util.LoopBundle;
import com.tinkerpop.pipes.util.EmptyIterator;
import com.tinkerpop.pipes.util.ExpandablePipe;
import com.tinkerpop.pipes.util.Pipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class LoopPipe<S> extends AbstractPipe<S, S> {

    protected final PipeFunction<?, Pipe<S, S>> pipeFunction;
    protected final PipeFunction<LoopBundle<S>, Boolean> whileFunction;
    protected final PipeFunction<LoopBundle<S>, Boolean> emitFunction;
    protected final Queue<S> endQueue = new LinkedList<S>();
    protected final Queue<Integer> stepQueue = new LinkedList<Integer>();
    protected int currentStep;

    final List<Pipeline<S, S>> pipes = new ArrayList<Pipeline<S, S>>();


    public LoopPipe(final PipeFunction<LoopBundle<S>, Boolean> whileFunction, final PipeFunction<?, Pipe<S, S>> pipeFunction, final PipeFunction<LoopBundle<S>, Boolean> emitFunction) {
        this.pipeFunction = pipeFunction;
        this.whileFunction = whileFunction;
        this.emitFunction = emitFunction;
        this.createInternalPipeline();
    }

    public LoopPipe(final PipeFunction<LoopBundle<S>, Boolean> whileFunction, final PipeFunction<?, Pipe<S, S>> pipeFunction) {
        this(whileFunction, pipeFunction, null);
    }

    public void setStarts(final Iterator<S> starts) {
        this.pipes.get(0).setStarts(starts);
    }

    public S processNextStart() {
        short done = 1;
        while (done != 3) {
            if (done == 2)
                done = 3;
            if (!this.endQueue.isEmpty()) {
                this.currentStep = this.stepQueue.remove();
                return this.endQueue.remove();
            } else {
                for (int i = 0; i < this.pipes.size(); i++) {
                    final Pipeline<S, S> pipe = pipes.get(i);
                    if (pipe.hasNext()) {
                        ((ExpandablePipe<S>) pipes.get(i + 1).getPipes().get(0)).add(pipe.next());
                        done = 1;
                    }
                }
            }
            if (done == 1)
                done = 2;
        }
        throw new NoSuchElementException();
    }

    public List getPath() {
        return this.getPathAtPipe(this.currentStep, this.currentEnd);
    }

    protected void createInternalPipeline() {
        final Pipeline<S, S> pipeline = new Pipeline<S, S>(new ExpandablePipe<S>(), new WhileFunctionTestPipe(this.pipes.size() + 1), this.pipeFunction.compute(null));
        pipeline.setStarts(new EmptyIterator<S>());
        this.pipes.add(pipeline);
    }

    protected List getPathAtPipe(final int step, final S s) {
        final List path = new LinkedList();
        for (int i = 0; i < step - 1; i++) {
            final List temp = pipes.get(i).getPath();
            temp.remove(null);
            path.addAll(temp.subList(0, temp.size() - 1));
        }
        path.add(s);
        return path;
    }


    private class WhileFunctionTestPipe extends AbstractPipe<S, S> {
        private final int step;

        public WhileFunctionTestPipe(final int step) {
            this.step = step;
        }

        public S processNextStart() {
            while (true) {
                final S s = this.starts.next();
                if (whileFunction.compute(new LoopBundle<S>(s, step))) {
                    if (pipes.size() <= step) {
                        createInternalPipeline();
                    }
                    if (emitFunction != null && emitFunction.compute(new LoopBundle<S>(s, step))) {
                        endQueue.add(s);
                        stepQueue.add(step);
                    }

                    return s;
                } else {
                    if (emitFunction != null && emitFunction.compute(new LoopBundle<S>(s, step))) {
                        endQueue.add(s);
                        stepQueue.add(step);
                    } else {
                        endQueue.add(s);
                        stepQueue.add(step);
                    }
                }
            }
        }

    }
}
