package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.AsPipe;
import com.tinkerpop.pipes.util.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TablePipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, Table> {

    private Table table;
    private final PipeFunction[] columnFunctions;
    private int currentFunction;
    private final List<AsPipe> asPipes = new ArrayList<AsPipe>();
    private final boolean doFunctions;

    public TablePipe(final Table table, final Collection<String> stepNames, final List<AsPipe> allPreviousAsPipes, final PipeFunction... columnFunctions) {
        this.table = table;
        this.columnFunctions = columnFunctions;

        if (this.doFunctions = this.columnFunctions.length > 0)
            currentFunction = 0;

        final List<String> tempNames = new ArrayList<String>();
        for (final AsPipe asPipe : allPreviousAsPipes) {
            final String columnName = asPipe.getName();
            if (null == stepNames || stepNames.contains(columnName)) {
                tempNames.add(columnName);
                this.asPipes.add(asPipe);
            }
        }

        if (tempNames.size() > 0)
            table.setColumnNames(tempNames.toArray(new String[tempNames.size()]));

    }

    public Table getSideEffect() {
        return this.table;
    }

    public S processNextStart() {
        final S s = this.starts.next();
        final List row = new ArrayList();
        for (final AsPipe asPipe : this.asPipes) {
            if (doFunctions) {
                row.add(this.columnFunctions[currentFunction++ % columnFunctions.length].compute(asPipe.getCurrentEnd()));
            } else {
                row.add(asPipe.getCurrentEnd());
            }
        }
        this.table.addRow(row);
        return s;
    }

    public void reset() {
        this.table = new Table();
        this.currentFunction = 0;
        super.reset();
    }
}
