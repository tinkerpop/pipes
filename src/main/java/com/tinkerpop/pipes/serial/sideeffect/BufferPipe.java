package com.tinkerpop.pipes.serial.sideeffect;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BufferPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, List<S>> {

    final private List<S> buffer = new ArrayList<S>();
    final private int maxBufferSize;

    public BufferPipe(final int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    protected S processNextStart() {
        S s = this.starts.next();
        this.addObjectToBuffer(s);
        return s;
    }

    private void addObjectToBuffer(final S s) {
        if (this.maxBufferSize != -1 && this.buffer.size() >= this.maxBufferSize) {
            this.buffer.remove(0);
        }
        this.buffer.add(s);
    }

    public List<S> getSideEffect() {
        return this.buffer;
    }
}
