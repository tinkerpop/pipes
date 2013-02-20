package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.AsMap;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class PipesFunction<A, B> implements PipeFunction<A, B> {

    protected AsMap asMap;

    public void setAsMap(final AsMap asMap) {
        this.asMap = asMap;
    }
}