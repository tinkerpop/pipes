package com.tinkerpop.pipes.util.structures;

import com.tinkerpop.pipes.PipeFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Table is a collection of rows with various table-style access methods.
 *
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
        Table table = new Table();
        for (final Row row : this) {
            List temp = new ArrayList();
            for (int i = 0; i < row.size(); i++) {
                temp.add(functions[i % functions.length].compute(row.get(i)));
            }
            table.addRow(temp);
        }
        return table;
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

    /**
     * Filter out duplicates according to a default comparator
     *
     * @return a newly created table with unique rows
     */
    public Table unique() {
        final Table temp = Table.cloneTableStructure(this);
        final SortedSet<Row> set = new TreeSet<Row>(Table.getDefaultRowComparator());
        for (final Row row : this) {
            set.add(row);
        }
        for (final Row row : set) {
            temp.addRow(row);
        }
        return temp;
    }

    /**
     * Filter out duplicates according to the  provided comparator
     *
     * @param comparator a row comparator
     * @return a newly created table with unique rows
     */
    public Table unique(final Comparator<Row> comparator) {
        final Table temp = Table.cloneTableStructure(this);
        final SortedSet<Row> set = new TreeSet<Row>(comparator);
        for (final Row row : this) {
            set.add(row);
        }
        for (final Row row : set) {
            temp.addRow(row);
        }
        return temp;
    }

    /**
     * Sort the rows of the table according to a default comparator
     *
     * @return a newly created sorted table
     */
    public Table sort() {
        final Table temp = Table.cloneTableStructure(this);
        final List<Row> rows = new ArrayList<Row>();
        for (final Row row : this) {
            rows.add(row);
        }
        Collections.sort(rows, Table.getDefaultRowComparator());
        temp.addAll(rows);
        return temp;
    }

    /**
     * Sort the rows of the table according to provided comparator
     *
     * @param comparator a row comparator
     * @return a newly created sorted table
     */
    public Table sort(Comparator<Row> comparator) {
        final Table temp = Table.cloneTableStructure(this);
        final List<Row> rows = new ArrayList<Row>();
        for (final Row row : this) {
            rows.add(row);
        }
        Collections.sort(rows, comparator);
        temp.addAll(rows);
        return temp;
    }

    /**
     * Create a new table with the same column names as provided table
     *
     * @param table a table
     * @return a new table with same column names
     */
    public static Table cloneTableStructure(final Table table) {
        if (table.getColumnNames().size() > 0)
            return new Table(table.columnNames.toArray(new String[table.columnNames.size()]));
        else
            return new Table();
    }

    public void clear() {
        super.clear();
        this.tableWidth = -1;
        this.columnNames = new ArrayList<String>();
    }

    private static Comparator<Row> getDefaultRowComparator() {
        return new Comparator<Row>() {
            @Override
            public int compare(final Row a, final Row b) {
                int comp = 0;
                for (int i = 0; i < a.size(); i++) {
                    comp = comp + ((Comparable) a.get(i)).compareTo(b.get(i));
                }
                return comp;
            }
        };
    }
}
