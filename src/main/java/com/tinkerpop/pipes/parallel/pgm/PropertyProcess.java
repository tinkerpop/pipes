package com.tinkerpop.pipes.parallel.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.parallel.Channel;
import com.tinkerpop.pipes.parallel.SerialProcess;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PropertyProcess<E> extends SerialProcess<Element, E> {

    private final String key;

    public PropertyProcess(final String key) {
        this(key, null, null);
    }

    public PropertyProcess(final String key, final Channel<Element> inChannel, final Channel<E> outChannel) {
        super(inChannel, outChannel);
        this.key = key;
    }

    public boolean step() {
        Element element = this.inChannel.read();
        if (null != element) {
            E value = (E) element.getProperty(this.key);
            if (null != value) {
                this.outChannel.write(value);
            }
            return true;
        } else {
            return false;
        }
    }

}
