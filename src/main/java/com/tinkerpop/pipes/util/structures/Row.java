package com.tinkerpop.pipes.util.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * A row with column names and table-style access patterns.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class Row<T> extends ArrayList<T> {

    private List<String> columnNames;

    public Row(final List<T> row, final List<String> columnNames) {
        super(row);
        this.columnNames = columnNames;
    }

    public Row(final List<String> columnNames) {
        super();
        this.columnNames = columnNames;
    }

    public Row() {
        super();
    }

    public String toString() {

        final StringBuilder buffer = new StringBuilder("[");
        for (int i = 0; i < this.size(); i++) {
            if (columnNames.size() > 0) {
                buffer.append(columnNames.get(i));
                buffer.append(":");
            }
            buffer.append(this.get(i));
            if (i < this.size() - 1)
                buffer.append(", ");
        }
        buffer.append("]");
        return buffer.toString();

    }

    public T getColumn(final String columnName) {
        return this.get(columnNames.indexOf(columnName));
    }

    public T getColumn(final int column) {
        return this.get(column);
    }

    public List<String> getColumnNames() {
        return this.columnNames;
    }
}
