package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TreePipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, Map> {

    Map tree = new HashMap();
    final List<PipeFunction> branchFunctions;
    int currentFunction = 0;


    public TreePipe(final PipeFunction... branchFunctions) {
        if (branchFunctions.length == 0)
            this.branchFunctions = null;
        else
            this.branchFunctions = Arrays.asList(branchFunctions);
    }

    public TreePipe(final Map tree, final PipeFunction... branchFunctions) {
        this(branchFunctions);
        this.tree = tree;
    }


    public void setStarts(Iterator<S> starts) {
        this.starts = starts;
        this.enablePath(true);
    }

    public S processNextStart() {
        final S s = this.starts.next();
        final List path = ((Pipe) this.starts).getCurrentPath();
        Map depth = this.tree;
        for (int i = 0; i < path.size(); i++) {
            Object object = path.get(i);
            if (null != this.branchFunctions) {
                object = this.branchFunctions.get(this.currentFunction).compute(object);
                this.currentFunction = (this.currentFunction + 1) % this.branchFunctions.size();
            }

            if (!depth.containsKey(object))
                depth.put(object, new HashMap());

            depth = (Map) depth.get(object);
        }
        return s;
    }

    public Map getSideEffect() {
        return this.tree;
    }

    public void reset() {
        this.tree = new HashMap();
        this.currentFunction = 0;
        super.reset();
    }


}
