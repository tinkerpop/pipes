package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.AsPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.structures.Row;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SelectPipe will emit a List of the objects that are at the respective 'as'-steps (i.e. partition steps) in the Pipeline.
 * In this way, it is possible to isolate particular parts of the Pipeline for emission.
 * If stepNames are provided the only those partition steps are emitted.
 * If stepFunctions are provided then each object of the step is processed by the function prior to emission.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SelectPipe<S> extends AbstractPipe<S, Row> implements TransformPipe<S, Row> {

    private final PipeFunction[] stepFunctions;
    private final boolean doFunctions;
    private int currentFunction;
    private final List<AsPipe> asPipes = new ArrayList<AsPipe>();
    private final List<String> columnNames;
    private final Collection<String> stepNames;

    public SelectPipe(final Collection<String> stepNames, final List<AsPipe> allPreviousAsPipes, final PipeFunction... stepFunctions) {
        this.stepFunctions = stepFunctions;
        this.stepNames = stepNames;

        if (this.doFunctions = this.stepFunctions.length > 0)
            currentFunction = 0;

        final List<String> tempNames = new ArrayList<String>();
        for (final AsPipe asPipe : allPreviousAsPipes) {
            final String columnName = asPipe.getName();
            if (null == this.stepNames || this.stepNames.contains(columnName)) {
                tempNames.add(columnName);
                this.asPipes.add(asPipe);
            }
        }

        if (tempNames.size() > 0)
            this.columnNames = tempNames;
        else
            this.columnNames = null;

    }

    public Row processNextStart() {
        this.starts.next();
        final Row row;
        if (null == columnNames)
            row = new Row();
        else
            row = new Row(columnNames);

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

    public String toString() {
        if (null == this.stepNames)
            return PipeHelper.makePipeString(this);
        else
            return PipeHelper.makePipeString(this, this.stepNames.toArray());
    }

}
