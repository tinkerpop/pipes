package com.tinkerpop.pipes.serial.util;

import com.tinkerpop.pipes.serial.AbstractPipe;
import com.tinkerpop.pipes.serial.Pipe;
import com.tinkerpop.pipes.serial.sideeffect.BufferPipe;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class HistoryPipe<S, E> extends AbstractPipe<S, E> {

    private final BufferPipe<E> bufferPipe;
    private final Pipe<S, ?> testPipe;

    public HistoryPipe(BufferPipe<E> bufferPipe, Pipe<S, ?> testPipe) {
        this.bufferPipe = bufferPipe;
        this.testPipe = testPipe;
    }

    protected E processNextStart() {
        while (this.starts.hasNext()) {
            this.testPipe.setStarts(Arrays.asList(this.starts.next()));
            if (this.testPipe.hasNext()) {
                this.testPipe.next();
                return this.bufferPipe.getSideEffect().get(0);
            }
        }
        throw new NoSuchElementException();
    }
}
