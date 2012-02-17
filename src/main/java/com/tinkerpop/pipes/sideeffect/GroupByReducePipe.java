package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupByReducePipe<S, K, V, V2> extends AbstractPipe<S, S> implements SideEffectPipe<S, Map<K, V2>> {

    private Map<K, List<V>> byMap = new HashMap<K, List<V>>();
    private Map<K, V2> reduceMap = new HashMap<K, V2>();
    private final PipeFunction<S, K> keyFunction;
    private final PipeFunction<S, V> valueFunction;
    private final PipeFunction<List<V>, V2> reduceFunction;

    public GroupByReducePipe(final PipeFunction<S, K> keyFunction, final PipeFunction<S, V> valueFunction, final PipeFunction<List<V>, V2> reduceFunction) {
        this.keyFunction = keyFunction;
        this.valueFunction = valueFunction;
        this.reduceFunction = reduceFunction;
    }

    protected S processNextStart() {
        final S s = this.starts.next();
        final K key = this.getKey(s);
        final V value = this.getValue(s);

        List<V> list = this.byMap.get(key);
        if (null == list) {
            list = new ArrayList<V>();
            list.add(value);
            this.byMap.put(key, list);
        } else {
            list.add(value);
        }

        if (!this.starts.hasNext()) {
            for (final Map.Entry<K, List<V>> entry : byMap.entrySet()) {
                this.reduceMap.put(entry.getKey(), reduceFunction.compute(entry.getValue()));
            }
            this.byMap.clear();
        }

        return s;
    }

    public Map<K, V2> getSideEffect() {
        return this.reduceMap;
    }

    private K getKey(final S start) {
        if (null == keyFunction) {
            return (K) start;
        } else {
            return keyFunction.compute(start);
        }
    }

    private V getValue(final S start) {
        if (null == valueFunction) {
            return (V) start;
        } else {
            return valueFunction.compute(start);
        }
    }

    public void reset() {
        this.byMap = new HashMap<K, List<V>>();
        this.reduceMap = new HashMap<K, V2>();
        super.reset();
    }
}
