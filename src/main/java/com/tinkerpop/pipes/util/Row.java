package com.tinkerpop.pipes.util;

import java.util.ArrayList;
import java.util.List;

/**
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

        final StringBuffer buffer = new StringBuffer("[");
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
}
