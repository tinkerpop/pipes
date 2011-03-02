package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdVertexPipe<S> extends AbstractPipe<S, Vertex> {

    private final Graph graph;

    public IdVertexPipe(final Graph graph) {
        this.graph = graph;
    }

  	@Override
  	public boolean hasNext() {
  		return this.starts.hasNext();
  	}

    protected Vertex processNextStart() {
        return this.graph.getVertex(this.starts.next());
    }
}
