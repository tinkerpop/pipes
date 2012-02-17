package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.iterators.ExpandableMultiIterator;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupByReducePipe<S, K, V, V2> extends AbstractPipe<S, S> implements SideEffectPipe<S, Map<K, V2>> {

    private Map<K, Iterator<V>> byMap = new HashMap<K, Iterator<V>>();
    private Map<K, V2> reduceMap = new HashMap<K, V2>();
    private final PipeFunction<S, K> keyFunction;
    private final PipeFunction<S, V> valueFunction;
    private final PipeFunction<Iterator<V>, V2> reduceFunction;

    public GroupByReducePipe(final PipeFunction<S, K> keyFunction, final PipeFunction<S, V> valueFunction, final PipeFunction<Iterator<V>, V2> reduceFunction) {
        this.keyFunction = keyFunction;
        this.valueFunction = valueFunction;
        this.reduceFunction = reduceFunction;
    }

    protected S processNextStart() {
        final S s = this.starts.next();
        final K key = this.getKey(s);
        final V value = this.getValue(s);

        ExpandableMultiIterator<V> values = (ExpandableMultiIterator<V>) this.byMap.get(key);
        if (null == values) {
            values = new ExpandableMultiIterator<V>();
            this.addValue(value, values);
            this.byMap.put(key, values);
        } else {
            this.addValue(value, values);
        }

        if (!this.starts.hasNext()) {
            for (final Map.Entry<K, Iterator<V>> entry : byMap.entrySet()) {
                this.reduceMap.put(entry.getKey(), reduceFunction.compute(entry.getValue()));
            }
            this.byMap.clear();
        }

        return s;
    }

    public void addValue(final V value, final ExpandableMultiIterator values) {
        if (value instanceof Pipe) {
            values.addIterator((Pipe) value);
        } else {
            values.addIterator(new SingleIterator<V>(value));
        }
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
        this.byMap = new HashMap<K, Iterator<V>>();
        this.reduceMap = new HashMap<K, V2>();
        super.reset();
    }
}
