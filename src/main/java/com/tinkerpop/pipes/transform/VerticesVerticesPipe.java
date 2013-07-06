package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class VerticesVerticesPipe extends AbstractPipe<Vertex, Vertex> implements TransformPipe<Vertex, Vertex> {

    protected int branchFactor;
    protected Direction direction;
    protected String[] labels;
    protected Iterator<Vertex> nextEnds = PipeHelper.emptyIterator();

    private final boolean doBranchFactor;

    public VerticesVerticesPipe(final Direction direction, final String... labels) {
        this(direction, Integer.MAX_VALUE, labels);
    }

    public VerticesVerticesPipe(final Direction direction, final int branchFactor, final String... labels) {
        this.direction = direction;
        this.branchFactor = branchFactor;
        this.labels = labels;
        this.doBranchFactor = branchFactor != Integer.MAX_VALUE;
    }

    public void reset() {
        this.nextEnds = PipeHelper.emptyIterator();
        super.reset();
    }


    protected Vertex processNextStart() {
        while (true) {
            if (this.nextEnds.hasNext()) {
                return this.nextEnds.next();
            } else {
                this.nextEnds = this.doBranchFactor ?
                        this.starts.next().query().direction(this.direction).labels(this.labels).limit(this.branchFactor).vertices().iterator() :
                        this.starts.next().getVertices(this.direction, this.labels).iterator();
            }
        }
    }

    public Direction getDirection() {
        return this.direction;
    }

    public String[] getLabels() {
        return this.labels;
    }

    public int getBranchFactor() {
        return this.branchFactor;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, direction.name().toLowerCase(), Arrays.asList(labels));
    }

}
