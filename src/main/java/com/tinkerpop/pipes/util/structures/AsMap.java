package com.tinkerpop.pipes.util.structures;

import com.tinkerpop.pipes.util.AsPipe;
import com.tinkerpop.pipes.util.FluentUtility;
import com.tinkerpop.pipes.util.MetaPipe;

import java.util.HashMap;
import java.util.Map;

/**
 * The current object at a named step in a PipesFluentPipeline can be accessed via AsMap.
 * AsMap maintains an internal HashMap that maps a String name to an AsPipe.
 * The AsPipe.getCurrentEnd() allows access to the last object to be emitted from the AsPipe.
 * This is used in conjunction with PipesFunction to allow PipeFunctions to access objects.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AsMap {

    private final MetaPipe metaPipe;
    private final Map<String, AsPipe> map = new HashMap<String, AsPipe>();

    public AsMap(final MetaPipe metaPipe) {
        this.metaPipe = metaPipe;
    }

    public void refresh() {
        for (final AsPipe asPipe : FluentUtility.getAsPipes(this.metaPipe)) {
            this.map.put(asPipe.getName(), asPipe);
        }
    }

    public Object get(final String name) {
        final AsPipe asPipe = this.map.get(name);
        if (null == asPipe)
            throw new RuntimeException("Named step does not exist: " + name);
        else
            return asPipe.getCurrentEnd();
    }

}
