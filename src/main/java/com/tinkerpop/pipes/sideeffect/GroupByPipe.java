package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupByPipe<S, K, V> extends AbstractPipe<S, S> implements SideEffectPipe<S, Map<K, List<V>>> {

    protected Map<K, List<V>> byMap;
    protected final PipeFunction<S, K> keyFunction;
    protected final PipeFunction<S, V> valueFunction;

    public GroupByPipe(final Map<K, List<V>> byMap, final PipeFunction<S, K> keyFunction, final PipeFunction<S, V> valueFunction) {
        this.byMap = byMap;
        this.keyFunction = keyFunction;
        this.valueFunction = valueFunction;
    }

    public GroupByPipe(final PipeFunction<S, K> keyFunction, final PipeFunction<S, V> valueFunction) {
        this(new HashMap<K, List<V>>(), keyFunction, valueFunction);
    }

    protected S processNextStart() {
        final S s = this.starts.next();
        final K key = this.getKey(s);
        final V value = this.getValue(s);

        List<V> list = this.byMap.get(key);
        if (null == list) {
            list = new ArrayList<V>();
            this.addValue(value, list);
            this.byMap.put(key, list);
        } else {
            this.addValue(value, list);
        }

        return s;
    }

    public Map<K, List<V>> getSideEffect() {
        return this.byMap;
    }

    public void addValue(final V value, final List values) {
        if (value instanceof Pipe) {
            for (final Object o : ((Pipe) value)) {
                values.add(o);
            }
        } else {
            values.add(value);
        }
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
        super.reset();
    }
}
