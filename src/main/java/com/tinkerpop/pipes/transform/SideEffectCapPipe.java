package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.sideeffect.SideEffectPipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.FastNoSuchElementException;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The SideEffectCapPipe will yield an E that is the side effect of the provided SideEffectPipe.
 * This is useful for when the side effect of a Pipe is desired in a computational stream.
 * Note that LazySideEffectPipes are handled differently from GreedySideEffectPipes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SideEffectCapPipe<S, T> extends AbstractMetaPipe<S, T> implements MetaPipe, TransformPipe<S, T> {

    private final SideEffectPipe<S, T> pipeToCap;
    private boolean alive = true;

    public SideEffectCapPipe(final SideEffectPipe<S, T> pipeToCap) {
        this.pipeToCap = pipeToCap;
    }

    public void setStarts(final Iterator<S> starts) {
        this.pipeToCap.setStarts(starts);
    }

    protected T processNextStart() {
        if (this.pipeToCap instanceof SideEffectPipe.LazySideEffectPipe) {
            this.pipeToCap.next();
            return this.pipeToCap.getSideEffect();
        } else {
            if (this.alive) {
                try {
                    while (true) {
                        this.pipeToCap.next();
                    }
                } catch (final NoSuchElementException e) {
                }
                this.alive = false;
                return this.pipeToCap.getSideEffect();
            } else {
                throw FastNoSuchElementException.instance();
            }
        }
    }

    public List getCurrentPath() {
        if (this.pathEnabled) {
            final List list = this.pipeToCap.getCurrentPath();
            list.add(this.currentEnd);
            return list;
        } else {
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipeToCap);
    }

    public List<Pipe> getPipes() {
        return Arrays.asList((Pipe) this.pipeToCap);
    }

    public void reset() {
        this.alive = true;
        super.reset();
    }
}
