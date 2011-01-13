package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * The EdgeVertexPipe returns either the incoming or outgoing Vertex of an Edge start.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class EdgeVertexPipe extends AbstractPipe<Edge, Vertex> {

    private final Step step;
    private Vertex next = null;

    public enum Step {
        IN_VERTEX, OUT_VERTEX, BOTH_VERTICES
    }

    public EdgeVertexPipe(final Step step) {
        if (null == step)
            throw new IllegalArgumentException("Step can not be null");
        this.step = step;
    }

    protected Vertex processNextStart() {
        switch (this.step) {
            case OUT_VERTEX:
                return this.starts.next().getOutVertex();
            case IN_VERTEX:
                return this.starts.next().getInVertex();
            case BOTH_VERTICES: {
                if (null == this.next) {
                    Edge edge = this.starts.next();
                    this.next = edge.getOutVertex();
                    return edge.getInVertex();
                } else {
                    Vertex temp = this.next;
                    this.next = null;
                    return temp;
                }
            }
        }
        throw new IllegalStateException("This is an illegal state as there is no step set");
    }

    public String toString() {
        return super.toString() + "<" + this.step + ">";
    }

}
