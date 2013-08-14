package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.PipeFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupByReducePipe<S, K, V, V2> extends GroupByPipe<S, K, V> {

    private Map<K, V2> reduceMap;
    private final PipeFunction<Collection<V>, V2> reduceFunction;

    public GroupByReducePipe(final PipeFunction<S, K> keyFunction, final PipeFunction<S, V> valueFunction, final PipeFunction<Collection<V>, V2> reduceFunction) {
        this(new HashMap<K, V2>(), keyFunction, valueFunction, reduceFunction);
    }

    public GroupByReducePipe(final Map<K, V2> reduceMap, final PipeFunction<S, K> keyFunction, final PipeFunction<S, V> valueFunction, final PipeFunction<Collection<V>, V2> reduceFunction) {
        super(new HashMap<K, Collection<V>>(), keyFunction, valueFunction);
        this.reduceMap = reduceMap;
        this.reduceFunction = reduceFunction;
    }

    protected S processNextStart() {
        final S s = super.processNextStart();
        if (!this.starts.hasNext()) {
            for (final Map.Entry<K, Collection<V>> entry : this.byMap.entrySet()) {
                this.reduceMap.put(entry.getKey(), this.reduceFunction.compute(entry.getValue()));
            }
        }
        return s;
    }

    public Map getSideEffect() {
        return this.reduceMap;
    }

    public void reset() {
        this.reduceMap = new HashMap<K, V2>();
        super.reset();
    }
}
