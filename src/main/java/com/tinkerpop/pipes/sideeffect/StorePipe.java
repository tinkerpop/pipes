package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.Collection;

/**
 * The StoragePipe yields its input, however, each input is added to the provided collection.
 * If a PipeFunction is provided then it process the object and stores the result of that process.
 * This pipe is similar to AggregatePipe except that is does not aggregate everything up to the current step.
 * Instead, it fills the storage collection as objects pass through it.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class StorePipe<S> extends AbstractPipe<S, S> implements SideEffectPipe.LazySideEffectPipe<S, Collection> {

    private Collection storage;
    private PipeFunction<S, ?> preStoreFunction = null;

    public StorePipe(final Collection storage) {
        this.storage = storage;
    }

    /**
     * The provided PipeFunction will process the object prior to inserting it into the storage collection.
     *
     * @param storage          the storage collection
     * @param preStoreFunction a function to process an object with prior to insertion into the collection
     */
    public StorePipe(final Collection storage, final PipeFunction<S, ?> preStoreFunction) {
        this(storage);
        this.preStoreFunction = preStoreFunction;
    }

    protected S processNextStart() {
        final S s = this.starts.next();
        if (null != this.preStoreFunction) {
            this.storage.add(this.preStoreFunction.compute(s));
        } else {
            this.storage.add(s);
        }
        return s;
    }

    public Collection getSideEffect() {
        return this.storage;
    }

    public void reset() {
        try {
            this.storage = this.storage.getClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        super.reset();
    }
}
