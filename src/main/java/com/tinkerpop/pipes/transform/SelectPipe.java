package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.AsPipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SelectPipe will emit a List of the objects that are at the respective 'as'-steps (i.e. named steps) in the Pipeline.
 * In this way, it is possible to isolate particular parts of the Pipeline for emission.
 * If stepNames are provided the only those named steps are emitted.
 * If stepFunctions are provided then each object of the step is processed by the function prior to emission.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SelectPipe<S> extends AbstractPipe<S, List> {

    private final PipeFunction[] stepFunctions;
    private final boolean doFunctions;
    private int currentFunction;
    private final List<AsPipe> asPipes = new ArrayList<AsPipe>();

    public SelectPipe(final Collection<String> stepNames, final List<AsPipe> allPreviousAsPipes, final PipeFunction... stepFunctions) {
        this.stepFunctions = stepFunctions;

        if (this.doFunctions = this.stepFunctions.length > 0)
            currentFunction = 0;

        for (final AsPipe asPipe : allPreviousAsPipes) {
            final String columnName = asPipe.getName();
            if (null == stepNames || stepNames.contains(columnName)) {
                this.asPipes.add(asPipe);
            }
        }
    }

    public List processNextStart() {
        this.starts.next();
        final List row = new ArrayList();
        for (final AsPipe asPipe : this.asPipes) {
            if (doFunctions) {
                row.add(this.stepFunctions[currentFunction++ % stepFunctions.length].compute(asPipe.getCurrentEnd()));
            } else {
                row.add(asPipe.getCurrentEnd());
            }
        }
        return row;
    }

    public void reset() {
        this.currentFunction = 0;
        super.reset();
    }

}
