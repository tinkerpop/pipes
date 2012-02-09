package com.tinkerpop.pipes.util.structures;

import com.tinkerpop.pipes.PipeFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class Table extends ArrayList<Row> {

    private List<String> columnNames;
    private int tableWidth = -1;

    public Table() {
        this.columnNames = new ArrayList<String>();
    }

    public Table(final String... columnNames) {
        this();
        this.columnNames.addAll(Arrays.asList(columnNames));
        this.tableWidth = columnNames.length;
    }

    public Table apply(final PipeFunction... functions) {
        if (tableWidth != -1 && functions.length == tableWidth) {
            Table table = new Table();
            for (final Row row : this) {
                List temp = new ArrayList();
                for (int i = 0; i < row.size(); i++) {
                    temp.add(functions[i].compute(row.get(i)));
                }
                table.addRow(temp);
            }
            return table;
        } else {
            throw new RuntimeException("Table width is " + this.tableWidth + " and functions length is " + functions.length);
        }
    }

    public void addRow(final List row) {
        if (this.tableWidth == -1) {
            this.tableWidth = row.size();
        } else {
            if (row.size() != tableWidth) {
                throw new RuntimeException("Table width is " + this.tableWidth + " and row width is " + row.size());
            }
        }
        this.add(new Row(row, this.getColumnNames()));
    }

    public void addRow(final Object... row) {
        this.addRow(Arrays.asList(row));
    }

    public void setColumnNames(final String... columnNames) {
        if (tableWidth != -1 && columnNames.length != tableWidth) {
            throw new RuntimeException("Table width is " + this.tableWidth + " and there are " + columnNames.length + " column names");
        }
        this.columnNames.clear();
        this.columnNames.addAll(Arrays.asList(columnNames));
        this.tableWidth = this.columnNames.size();
    }

    public List<String> getColumnNames() {
        return this.columnNames;
    }

    public int getRowCount() {
        return this.size();
    }

    public int getColumnCount() {
        return tableWidth;
    }

    public Object get(final int row, final int column) {
        return this.get(row).get(column);
    }

    public Object get(final int row, final String columnName) {
        return this.get(row).get(this.columnNames.indexOf(columnName));
    }

    public Row getRow(final int row) {
        return this.get(row);
    }

    public List getColumn(final int column) {
        final List temp = new ArrayList();
        for (final Row row : this) {
            temp.add(row.get(column));
        }
        return temp;
    }

    public List getColumn(final String columnName) {
        return this.getColumn(this.columnNames.indexOf(columnName));
    }

    public void clear() {
        super.clear();
        this.tableWidth = -1;
        this.columnNames = new ArrayList<String>();
    }
}
