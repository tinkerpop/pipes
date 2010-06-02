package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The VertexEdgePipe returns either the incoming or outgoing Edges of the Vertex start.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class VertexEdgePipe extends AbstractPipe<Vertex, Edge> {

    protected Iterator<Edge> nextEnds;
    private final Step step;

    public enum Step {
        OUT_EDGES, IN_EDGES, BOTH_EDGES
    }

    public VertexEdgePipe(final Step step) {
        this.step = step;
    }

    protected Edge processNextStart() {
        if (null != this.nextEnds && this.nextEnds.hasNext()) {
            return this.nextEnds.next();
        } else {
            if (this.step.equals(Step.OUT_EDGES))
                this.nextEnds = this.starts.next().getOutEdges().iterator();
            else if (this.step.equals(Step.IN_EDGES))
                this.nextEnds = this.starts.next().getInEdges().iterator();
            else {
                Vertex vertex = this.starts.next();
                this.nextEnds = new MultiIterator<Edge>(vertex.getInEdges().iterator(), vertex.getOutEdges().iterator());
            }
            return this.processNextStart();
        }
    }

    private class MultiIterator<T> implements Iterator<T> {

        private final Iterator<T> itty1;
        private final Iterator<T> itty2;

        public MultiIterator(Iterator<T> itty1, Iterator<T> itty2) {
            this.itty1 = itty1;
            this.itty2 = itty2;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public T next() {
            if (itty1.hasNext()) {
                return itty1.next();
            } else if (itty2.hasNext()) {
                return itty2.next();
            } else {
                throw new NoSuchElementException();
            }
        }

        public boolean hasNext() {
            return itty1.hasNext() || itty2.hasNext();
        }


    }
}