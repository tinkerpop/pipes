package com.tinkerpop.pipes.serial.sideeffect;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SideEffectCapPipe<S, T> extends AbstractPipe<S, T> {

    private final SideEffectPipe<S, ?, T> pipeToCap;

    public SideEffectCapPipe(SideEffectPipe<S, ?, T> pipeToCap) {
        this.pipeToCap = pipeToCap;
    }

    public void setStarts(Iterator<S> starts) {
        this.pipeToCap.setStarts(starts);
    }


    protected T processNextStart() {
        if (this.pipeToCap.hasNext()) {
            while (this.pipeToCap.hasNext()) {
                this.pipeToCap.next();
            }
            return this.pipeToCap.getSideEffect();
        } else {
            throw new NoSuchElementException();
        }
    }

}
