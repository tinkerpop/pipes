package com.tinkerpop.pipes.merge;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

/**
 * An ExhaustiveMergePipe will exhaust all the objects in one of its incoming pipes before moving to the next incoming pipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExhaustiveMergePipe<S> extends AbstractMergePipe<S> {

    protected S processNextStart() {
        if (null != this.currentEnds && this.currentEnds.hasNext()) {
            return this.currentEnds.next();
        } else {
            if ((null == this.currentEnds || !this.currentEnds.hasNext()) && this.starts.hasNext()) {
                this.currentEnds = this.starts.next();
                return processNextStart();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
