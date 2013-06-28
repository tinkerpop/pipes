package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * ToStringPipe turns an stream of objects into their Object.toString() representation.
 *
 * @author pietermartin
 */
public class ToStringPipe<S> extends AbstractPipe<S, String> implements TransformPipe<S, String> {

    private Iterator<Object> tempIterator = PipeHelper.emptyIterator();

    @Override
    protected String processNextStart() throws NoSuchElementException {
        while (true) {
            if (this.tempIterator.hasNext()) {
                return this.tempIterator.next().toString();
            } else {
                final Object result = this.starts.next();
                if (result instanceof Iterator) {
                    this.tempIterator = (Iterator) result;
                } else if (result instanceof Iterable) {
                    this.tempIterator = ((Iterable) result).iterator();
                } else if (result instanceof Object[]) {
                    this.tempIterator = new ArrayIterator((Object[]) result);
                } else if (result instanceof Map) {
                    this.tempIterator = ((Map) result).entrySet().iterator();
                } else {
                    return result.toString();
                }
            }
        }
    }

    @Override
    public void reset() {
        this.tempIterator = PipeHelper.emptyIterator();
        super.reset();
    }

    class ArrayIterator implements Iterator {

        private final Object[] array;
        private int count = 0;

        public ArrayIterator(final Object[] array) {
            this.array = array;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Object next() {
            if (count > array.length)
                throw new NoSuchElementException();

            return array[count++];
        }

        public boolean hasNext() {
            return count < array.length;
        }
    }

}
