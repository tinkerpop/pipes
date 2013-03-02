package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesPipeline;
import com.tinkerpop.pipes.util.StartPipe;
import com.tinkerpop.pipes.util.iterators.EmptyIterator;
import com.tinkerpop.pipes.util.structures.Pair;

import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrderMapPipe<S> extends AbstractPipe<Map<S, ?>, S> implements TransformPipe<Map<S, ?>, S> {

    private Pipe pipe = new StartPipe(EmptyIterator.INSTANCE);
    private final PipeFunction<Pair<Map.Entry<S, ?>, Map.Entry<S, ?>>, Integer> compareFunction;

    public OrderMapPipe(final PipeFunction<Pair<Map.Entry<S, ?>, Map.Entry<S, ?>>, Integer> compareFunction) {
        this.compareFunction = compareFunction;
    }

    public OrderMapPipe(final TransformPipe.Order order) {
        this.compareFunction = order.equals(OrderPipe.Order.DECR) ? new IncrementFunction() : new DecrementFunction();
    }

    public S processNextStart() {
        while (true) {
            if (!this.pipe.hasNext())
                this.pipe = new PipesPipeline(this.starts.next().entrySet()).order(this.compareFunction).transform(new PipeFunction<Map.Entry, Object>() {
                    public Object compute(final Map.Entry entry) {
                        return entry.getKey();
                    }
                });
            else
                return (S) this.pipe.next();
        }
    }

    private class DecrementFunction implements PipeFunction<Pair<Map.Entry<S, ?>, Map.Entry<S, ?>>, Integer> {
        public Integer compute(Pair<Map.Entry<S, ?>, Map.Entry<S, ?>> pair) {
            return ((Comparable) pair.getA().getValue()).compareTo(pair.getB().getValue());
        }
    }

    private class IncrementFunction implements PipeFunction<Pair<Map.Entry<S, ?>, Map.Entry<S, ?>>, Integer> {
        public Integer compute(Pair<Map.Entry<S, ?>, Map.Entry<S, ?>> pair) {
            return ((Comparable) pair.getB().getValue()).compareTo(pair.getA().getValue());
        }
    }
}

