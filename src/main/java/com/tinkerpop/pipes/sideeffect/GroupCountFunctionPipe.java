package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * GroupCountFunctionPipe is analogous to GroupCountPipe save that it takes two optional functions.
 * The first function is a key function which determines the key to use for each incoming object.
 * The second function is a value function which determines the value to put into the Map for each key.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupCountFunctionPipe<S, K> extends AbstractPipe<S, S> implements SideEffectPipe.GreedySideEffectPipe<S, Map<K, Number>> {

    private Map<K, Number> countMap;
    private final PipeFunction<Pair<S, Number>, Number> valueFunction;
    private final PipeFunction<S, K> keyFunction;

    public GroupCountFunctionPipe(final Map<K, Number> countMap, final PipeFunction<S, K> keyFunction, final PipeFunction<Pair<S, Number>, Number> valueFunction) {
        this.countMap = countMap;
        this.valueFunction = valueFunction;
        this.keyFunction = keyFunction;
    }

    public GroupCountFunctionPipe(final PipeFunction<S, K> keyFunction, final PipeFunction<Pair<S, Number>, Number> valueFunction) {
        this(new HashMap<K, Number>(), keyFunction, valueFunction);
    }

    protected S processNextStart() {
        final S s = this.starts.next();
        final K key = this.getKey(s);
        this.countMap.put(key, this.getValue(s, key));
        return s;
    }

    public Map<K, Number> getSideEffect() {
        return this.countMap;
    }

    public void reset() {
        try {
            this.countMap = this.countMap.getClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        super.reset();
    }

    private K getKey(final S start) {
        if (null == keyFunction) {
            return (K) start;
        } else {
            return keyFunction.compute(start);
        }
    }

    //TODO: Fix java.lang.Number issue.
    private Number getValue(final S start, final K key) {
        Number number = this.countMap.get(key);
        if (null == number) {
            number = 0l;
        }
        if (null == valueFunction) {
            return 1l + number.longValue();
        } else {
            return this.valueFunction.compute(new Pair<S, Number>(start, number));
        }

    }
}