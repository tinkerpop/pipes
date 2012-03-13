package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TreePipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, Map> {

    Map tree = new HashMap();

    public void setStarts(Iterator<S> starts) {
        this.starts = starts;
        this.enablePath(true);
    }

    public S processNextStart() {
        final S s = this.starts.next();
        final List path = ((Pipe) this.starts).getCurrentPath();
        Map depth = this.tree;
        for (int i = 0; i < path.size(); i++) {
            final Object object = path.get(i);
            if (!depth.containsKey(object))
                depth.put(object, new HashMap());
            depth = (Map) depth.get(object);
        }
        return s;
    }

    public Map getSideEffect() {
        return this.tree;
    }


}
