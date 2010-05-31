package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.HashMap;
import java.util.Map;

/**
 * The CountCombinePipe will simply emit the incoming object, but generate a map side effect.
 * The map's keys are the objects that come into the pipe.
 * The map's values are the number of times that the key object has come into the pipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CountCombinePipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, Map<S, Long>> {

    private final Map<S, Long> countMap = new HashMap<S, Long>();

    protected S processNextStart() {
        S s = this.starts.next();
        this.updateMap(s);
        return s;
    }

    public Map<S, Long> getSideEffect() {
        return this.countMap;
    }

    private void updateMap(final S s) {
        final Long temp = this.countMap.get(s);
        if (null == temp) {
            this.countMap.put(s, 1l);
        } else {
            this.countMap.put(s, 1l + temp);
        }

    }
}
