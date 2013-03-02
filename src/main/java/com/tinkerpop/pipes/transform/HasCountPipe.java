package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.FastNoSuchElementException;

/**
 * @author Darrick Wiebe (http://ofallpossibleworlds.wordpress.com)
 */
public class HasCountPipe<S> extends AbstractPipe<S, Boolean> implements TransformPipe<S, Boolean> {

    private long minimum;
    private long maximum;
    private long counter = 0;
    private boolean finished = false;

    /**
     * The pipe must emit at least min and not more than max elements.
     *
     * @param min minimum number of elements. Use -1 for no minimum.
     * @param max maximum number of elements. Use -1 for no maximum.
     */
    public HasCountPipe(long min, long max) {
        this.minimum = min;
        this.maximum = max;
    }

    public Boolean processNextStart() {
        if (this.finished) {
            throw FastNoSuchElementException.instance();
        }
        this.finished = true;
        if (this.minimum == -1 && this.maximum == -1)
            return Boolean.TRUE;
        while (true) {
            if (this.starts.hasNext()) {
                if (this.counter == this.maximum) {
                    return Boolean.FALSE;
                }
                this.counter++;
                if (this.minimum != -1 && this.counter == this.minimum && this.maximum == -1) {
                    return Boolean.TRUE;
                }
                this.starts.next();
            } else if (this.maximum != -1 && this.counter >= this.minimum) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }

    public void reset() {
        super.reset();
        this.counter = 0;
        this.finished = false;
    }
}
