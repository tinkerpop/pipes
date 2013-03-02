package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.Tree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TreePipe<S> extends AbstractPipe<S, S> implements SideEffectPipe.GreedySideEffectPipe<S, Map> {

    Tree<Object> tree = new Tree<Object>();
    final List<PipeFunction> branchFunctions;
    int currentFunction = 0;


    public TreePipe(final PipeFunction... branchFunctions) {
        if (branchFunctions.length == 0)
            this.branchFunctions = null;
        else
            this.branchFunctions = Arrays.asList(branchFunctions);
    }

    public TreePipe(final Tree tree, final PipeFunction... branchFunctions) {
        this(branchFunctions);
        this.tree = tree;
    }


    public void setStarts(Iterator<S> starts) {
        super.setStarts(starts);
        this.enablePath(true);
    }

    public S processNextStart() {
        final S s = this.starts.next();
        final List path = ((Pipe) this.starts).getCurrentPath();
        Tree<Object> depth = this.tree;
        for (int i = 0; i < path.size(); i++) {
            Object object = path.get(i);
            if (null != this.branchFunctions) {
                object = this.branchFunctions.get(this.currentFunction).compute(object);
                this.currentFunction = (this.currentFunction + 1) % this.branchFunctions.size();
            }

            if (!depth.containsKey(object))
                depth.put(object, new Tree());

            depth = depth.get(object);
        }
        return s;
    }

    public Map getSideEffect() {
        return this.tree;
    }

    public void reset() {
        this.tree = new Tree<Object>();
        this.currentFunction = 0;
        super.reset();
    }


}
