package com.tinkerpop.pipes.serial.util;

import com.tinkerpop.pipes.serial.AbstractPipe;
import com.tinkerpop.pipes.serial.filter.FilterPipe;
import com.tinkerpop.pipes.serial.sideeffect.BufferPipe;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class HistoryPipe<S, E> extends AbstractPipe<S, E> {

    private final BufferPipe<E> bufferPipe;
    private final FilterPipe<S, ?> filterPipe;

    public HistoryPipe(BufferPipe<E> bufferPipe, FilterPipe<S, ?> filterPipe) {
        this.bufferPipe = bufferPipe;
        this.filterPipe = filterPipe;
    }

    protected E processNextStart() {
        while (this.starts.hasNext()) {
            this.filterPipe.setStarts(Arrays.asList(this.starts.next()));
            if (this.filterPipe.hasNext()) {
                this.filterPipe.next();
                return this.bufferPipe.getSideEffect().get(0);
            }
        }
        throw new NoSuchElementException();
    }
}
