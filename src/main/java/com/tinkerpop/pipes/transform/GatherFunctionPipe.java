package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.FastNoSuchElementException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GatherFunctionPipe emits all the objects up to this step as an ArrayList.
 * This pipe is useful for doing breadth-first traversal where a List of all the current steps objects are gathered up.
 * This gathered up List can then be filtered by the provided postFilterFunction and thus, a selective branch breadth-first traversal can be enacted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherFunctionPipe<S, E> extends AbstractPipe<S, E> implements TransformPipe<S, E> {

    private List<List> listPaths = new ArrayList<List>();
    private final PipeFunction<List<S>, E> postFilterFunction;

    public GatherFunctionPipe(final PipeFunction<List<S>, E> postFilterFunction) {
        this.postFilterFunction = postFilterFunction;
    }

    public List getCurrentPath() {
        if (this.pathEnabled)
            return new ArrayList(this.listPaths);
        else
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
    }

    protected E processNextStart() {
        final List<S> list = new ArrayList<S>();
        this.listPaths = new ArrayList<List>();
        if (!this.starts.hasNext()) {
            throw FastNoSuchElementException.instance();
        } else {
            while (this.starts.hasNext()) {
                final S s = this.starts.next();
                list.add(s);
                if (this.pathEnabled)
                    this.listPaths.add(super.getPathToHere());
            }
        }

        if (this.pathEnabled) {
            E e = this.postFilterFunction.compute(list);
            if (e instanceof List)
                addList((List) e);
            else
                addList(Arrays.asList(e));
            return e;
            //return (E) addList((List) this.postFilterFunction.compute(list));
        } else {

            return this.postFilterFunction.compute(list);

        }
    }

    public void reset() {
        this.listPaths = new ArrayList<List>();
        super.reset();
    }

    private List addList(final List list) {
        for (final List l : this.listPaths) {
            l.add(list);
        }
        return list;
    }
}
