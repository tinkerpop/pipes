package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.AsMap;

/**
 * PipesFunction is a PipeFunction with the extra capability of maintaining a reference to an AsMap.
 * The asMap is available as the asMap field or via PipesFunction.getAsMap().
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class PipesFunction<A, B> implements PipeFunction<A, B> {

    protected AsMap asMap;

    public void setAsMap(final AsMap asMap) {
        this.asMap = asMap;
    }

    public AsMap getAsMap() {
        return this.asMap;
    }
}