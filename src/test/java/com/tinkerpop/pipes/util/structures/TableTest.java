package com.tinkerpop.pipes.util.structures;

import com.tinkerpop.pipes.PipeFunction;
import junit.framework.TestCase;

import java.util.Comparator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TableTest extends TestCase {

    public void testBasicTableConstruction() {

        Table table = new Table("name", "age");
        table.addRow("marko", 31);
        table.addRow("jen", 28);
        table.addRow("puppy", 6);

        assertEquals(table.get(0, 0), "marko");
        assertEquals(table.get(0, "name"), "marko");
        assertEquals(table.get(0, 1), 31);
        assertEquals(table.get(0, "age"), 31);

        assertEquals(table.get(1, 0), "jen");
        assertEquals(table.get(1, "name"), "jen");
        assertEquals(table.get(1, 1), 28);
        assertEquals(table.get(1, "age"), 28);

        assertEquals(table.get(2, 0), "puppy");
        assertEquals(table.get(2, "name"), "puppy");
        assertEquals(table.get(2, 1), 6);
        assertEquals(table.get(2, "age"), 6);

        assertEquals(table.getColumn("name").get(0), "marko");
        assertEquals(table.getColumn("name").get(1), "jen");
        assertEquals(table.getColumn("name").get(2), "puppy");

        assertEquals(table.getColumn("age").get(0), 31);
        assertEquals(table.getColumn("age").get(1), 28);
        assertEquals(table.getColumn("age").get(2), 6);

        assertEquals(table.getColumn(0).get(0), "marko");
        assertEquals(table.getColumn(0).get(1), "jen");
        assertEquals(table.getColumn(0).get(2), "puppy");

        assertEquals(table.getColumn(1).get(0), 31);
        assertEquals(table.getColumn(1).get(1), 28);
        assertEquals(table.getColumn(1).get(2), 6);

        assertEquals(table.getRow(0).get(0), "marko");
        assertEquals(table.getRow(0).get(1), 31);
        assertEquals(table.getRow(1).get(0), "jen");
        assertEquals(table.getRow(1).get(1), 28);
        assertEquals(table.getRow(2).get(0), "puppy");
        assertEquals(table.getRow(2).get(1), 6);

        assertEquals(table.getRow(0).getColumn("name"), "marko");
        assertEquals(table.getRow(0).getColumn("age"), 31);
        assertEquals(table.getRow(1).getColumn("name"), "jen");
        assertEquals(table.getRow(1).getColumn("age"), 28);
        assertEquals(table.getRow(2).getColumn("name"), "puppy");
        assertEquals(table.getRow(2).getColumn("age"), 6);

        assertEquals(table.getColumnCount(), 2);
        assertEquals(table.getRowCount(), 3);

        assertEquals(table.getRow(0).toString(), "[name:marko, age:31]");
        assertEquals(table.getRow(1).toString(), "[name:jen, age:28]");
        assertEquals(table.getRow(2).toString(), "[name:puppy, age:6]");


        assertEquals(table.getColumnNames().get(0), "name");
        assertEquals(table.getColumnNames().get(1), "age");
        table.setColumnNames("a-name", "an-age");
        assertEquals(table.getColumnNames().get(0), "a-name");
        assertEquals(table.getColumnNames().get(1), "an-age");

        assertEquals(table.getRow(0).toString(), "[a-name:marko, an-age:31]");
        assertEquals(table.getRow(1).toString(), "[a-name:jen, an-age:28]");
        assertEquals(table.getRow(2).toString(), "[a-name:puppy, an-age:6]");

        assertEquals(table.getRow(0).getColumn("a-name"), "marko");
        assertEquals(table.getRow(0).getColumn("an-age"), 31);
        assertEquals(table.getRow(1).getColumn("a-name"), "jen");
        assertEquals(table.getRow(1).getColumn("an-age"), 28);
        assertEquals(table.getRow(2).getColumn("a-name"), "puppy");
        assertEquals(table.getRow(2).getColumn("an-age"), 6);

        assertEquals(table.getRow(0).getColumn(0), "marko");
        assertEquals(table.getRow(0).getColumn(1), 31);
        assertEquals(table.getRow(1).getColumn(0), "jen");
        assertEquals(table.getRow(1).getColumn(1), 28);
        assertEquals(table.getRow(2).getColumn(0), "puppy");
        assertEquals(table.getRow(2).getColumn(1), 6);


    }

    public void testConstructionException() {
        try {
            Table table = new Table("name", "age");
            table.addRow("marko", 31, 365);
            assertFalse(true);
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        try {
            Table table = new Table();
            table.addRow("marko", 31);
            table.addRow("jen", 28, "ads");
            assertFalse(true);
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        try {
            Table table = new Table("name", "age");
            table.setColumnNames("name", "age", "history");
            assertFalse(true);
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    public void testUniqueMethods() {

        Table table = new Table("name", "age");
        table.addRow("marko", 32);
        table.addRow("marko", 32);
        table.addRow("jen", 28);
        assertEquals(table.size(), 3);
        Table temp = table.unique();
        assertEquals(temp.size(), 2);
        assertEquals(table.size(), 3);

        temp = table.unique(new Comparator<Row>() {
            @Override
            public int compare(Row a, Row b) {
                return -1;
            }
        });
        assertEquals(temp.size(), 3);
        assertEquals(table.size(), 3);


    }

    public void testSortMethods() {
        Table table = new Table("name", "age");
        table.addRow("adam", 3);
        table.addRow("bobby", 2);
        table.addRow("cabby", 1);
        assertEquals(table.size(), 3);
        assertEquals(table.get(0, 0), "adam");
        assertEquals(table.get(1, 0), "bobby");
        assertEquals(table.get(2, 0), "cabby");

        Table temp = table.sort();
        assertEquals(temp.size(), 3);
        assertEquals(temp.get(0, 0), "adam");
        assertEquals(temp.get(1, 0), "bobby");
        assertEquals(temp.get(2, 0), "cabby");

        temp = table.sort(new Comparator<Row>() {
            public int compare(Row a, Row b) {
                return ((Comparable) a.get(1)).compareTo(b.get(1));
            }
        });
        assertEquals(temp.size(), 3);
        assertEquals(temp.get(2, 0), "adam");
        assertEquals(temp.get(1, 0), "bobby");
        assertEquals(temp.get(0, 0), "cabby");

        assertEquals(table.size(), 3);
        assertEquals(table.get(0, 0), "adam");
        assertEquals(table.get(1, 0), "bobby");
        assertEquals(table.get(2, 0), "cabby");

    }

    public void testApplyMethod() {
        Table table = new Table("name", "location");
        table.addRow("marko", "santa fe");
        table.addRow("matthias", "san francisco");
        table.addRow("bobby", "chicago");

        Table temp = table.apply(new PipeFunction<String, Integer>() {
            public Integer compute(String string) {
                return string.length();
            }
        });
        assertEquals(temp.get(0, 0), 5);
        assertEquals(temp.get(0, 1), 8);
        assertEquals(temp.get(1, 0), 8);
        assertEquals(temp.get(1, 1), 13);
        assertEquals(temp.get(2, 0), 5);
        assertEquals(temp.get(2, 1), 7);

        assertEquals(table.get(0, 0), "marko");
        assertEquals(table.get(0, 1), "santa fe");
        assertEquals(table.get(1, 0), "matthias");
        assertEquals(table.get(1, 1), "san francisco");
        assertEquals(table.get(2, 0), "bobby");
        assertEquals(table.get(2, 1), "chicago");

        temp = table.apply(new PipeFunction<String, Integer>() {
                               public Integer compute(String string) {
                                   return string.length();
                               }
                           },
                new PipeFunction<String, String>() {
                    public String compute(String string) {
                        return string.substring(0, 1);
                    }
                }
        );

        assertEquals(temp.get(0, 0), 5);
        assertEquals(temp.get(0, 1), "s");
        assertEquals(temp.get(1, 0), 8);
        assertEquals(temp.get(1, 1), "s");
        assertEquals(temp.get(2, 0), 5);
        assertEquals(temp.get(2, 1), "c");


    }
}
