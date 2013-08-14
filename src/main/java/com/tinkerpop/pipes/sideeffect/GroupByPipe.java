package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupByPipe<S, K, V> extends AbstractPipe<S, S> implements SideEffectPipe.GreedySideEffectPipe<S, Map<K, List<V>>> {

    protected Map<K, Collection<V>> byMap;
    protected final PipeFunction<S, K> keyFunction;
    protected final PipeFunction<S, V> valueFunction;

    public GroupByPipe(final Map<K, Collection<V>> byMap, final PipeFunction<S, K> keyFunction, final PipeFunction<S, V> valueFunction) {
        this.byMap = byMap;
        this.keyFunction = keyFunction;
        this.valueFunction = valueFunction;
    }

    public GroupByPipe(final PipeFunction<S, K> keyFunction, final PipeFunction<S, V> valueFunction) {
        this(new HashMap<K, Collection<V>>(), keyFunction, valueFunction);
    }

    protected S processNextStart() {
        final S s = this.starts.next();
        final K key = this.getKey(s);
        final V value = this.getValue(s);

        Collection<V> list = this.byMap.get(key);
        if (null == list) {
            list = new ArrayList<V>();
            this.byMap.put(key, list);
        }
        this.addValue(value, list);
        return s;
    }

    public Map getSideEffect() {
        return this.byMap;
    }

    public void addValue(final V value, final Collection values) {
        if (value instanceof Pipe) {
            PipeHelper.fillCollection((Pipe) value, values);
        } else {
            values.add(value);
        }
    }

    protected K getKey(final S start) {
        return (null == this.keyFunction) ? (K) start : this.keyFunction.compute(start);
    }

    protected V getValue(final S start) {
        return (null == this.valueFunction) ? (V) start : this.valueFunction.compute(start);
    }

    public void reset() {
        this.byMap = new HashMap<K, Collection<V>>();
        super.reset();
    }
}
