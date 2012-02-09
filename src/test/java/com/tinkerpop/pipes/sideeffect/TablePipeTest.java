package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesPipeline;
import com.tinkerpop.pipes.util.structures.Table;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TablePipeTest extends TestCase {

    public void testTablePipe() throws Exception {

        Table t = new Table();
        new PipesPipeline(Arrays.asList("a", "b")).as("1").step(new StringCharPipe()).as("2").table(t).iterate();

        assertEquals(t.get(0, 0), "a");
        assertEquals(t.get(0, 1), "a");
        assertEquals(t.get(1, 0), "a");
        assertEquals(t.get(1, 1), "aa");
        assertEquals(t.get(2, 0), "b");
        assertEquals(t.get(2, 1), "b");
        assertEquals(t.get(3, 0), "b");
        assertEquals(t.get(3, 1), "bb");
        assertEquals(t.getColumnCount(), 2);
        assertEquals(t.getRowCount(), 4);

        t = new Table();
        new PipesPipeline(Arrays.asList("a", "b")).as("1").step(new StringCharPipe()).as("2").table(t, new PipeFunction<String, Integer>() {
            public Integer compute(String s) {
                return s.length();
            }

        }).iterate();

        assertEquals(t.get(0, 0), 1);
        assertEquals(t.get(0, 1), 1);
        assertEquals(t.get(1, 0), 1);
        assertEquals(t.get(1, 1), 2);
        assertEquals(t.get(2, 0), 1);
        assertEquals(t.get(2, 1), 1);
        assertEquals(t.get(3, 0), 1);
        assertEquals(t.get(3, 1), 2);
        assertEquals(t.getColumnCount(), 2);
        assertEquals(t.getRowCount(), 4);

    }

    public void testTablePipeWithMetaPipes() {
        Table t = new Table();
        new PipesPipeline(Arrays.asList("a")).as("1").step(new StringLengthPipe()).as("2").back(1).step(new StringCharPipe()).as("3").table(t).iterate();
        assertEquals(t.get(0, 0), "a");
        assertEquals(t.get(0, 1), 1);
        assertEquals(t.get(0, 2), "a");
        assertEquals(t.get(1, 0), "a");
        assertEquals(t.get(1, 1), 1);
        assertEquals(t.get(1, 2), "aa");
        assertEquals(t.getColumnCount(), 3);
        assertEquals(t.getRowCount(), 2);
    }

    /*public void testTablePipeWithLoop() {
        Table t = new Table();

        new PipesPipeline("a").as("1").step(new StringCharPipe()).loop(1, new PipeFunction<LoopPipe.LoopBundle, Boolean>() {
            @Override
            public Boolean compute(LoopPipe.LoopBundle argument) {
                return argument.getLoops() < 3;
            }
        }).as("2").table(t).iterate();

        assertEquals(t.get(0, 0), "a");
        assertEquals(t.get(0, 1), "a");
        assertEquals(t.get(1, 0), "a");
        assertEquals(t.get(1, 1), "aa");
        assertEquals(t.get(2, 0), "a");
        assertEquals(t.get(2, 1), "aa");
        assertEquals(t.get(3, 0), "a");
        assertEquals(t.get(3, 1), "aaaa");
        assertEquals(t.getColumnCount(), 2);
        assertEquals(t.getRowCount(), 4);
    }*/

    public void testTableFunctions() {

        Table t = new Table();
        new PipesPipeline(Arrays.asList("a", "b")).as("1").step(new StringCharPipe()).as("2").table(t).iterate();

        assertEquals(t.get(0, 0), "a");
        assertEquals(t.get(0, 1), "a");
        assertEquals(t.get(1, 0), "a");
        assertEquals(t.get(1, 1), "aa");
        assertEquals(t.get(2, 0), "b");
        assertEquals(t.get(2, 1), "b");
        assertEquals(t.get(3, 0), "b");
        assertEquals(t.get(3, 1), "bb");
        assertEquals(t.getColumnCount(), 2);
        assertEquals(t.getRowCount(), 4);

        t = t.apply(new PipeFunction<String, Integer>() {
                    public Integer compute(String a) {
                        return a.length();
                    }
                }, new PipeFunction<String, String>() {
            public String compute(String a) {
                return a + a;

            }
        }
        );

        assertEquals(t.get(0, 0), 1);
        assertEquals(t.get(0, 1), "aa");
        assertEquals(t.get(1, 0), 1);
        assertEquals(t.get(1, 1), "aaaa");
        assertEquals(t.get(2, 0), 1);
        assertEquals(t.get(2, 1), "bb");
        assertEquals(t.get(3, 0), 1);
        assertEquals(t.get(3, 1), "bbbb");
        assertEquals(t.getColumnCount(), 2);
        assertEquals(t.getRowCount(), 4);
    }

    public void testTablePipeWithSubColumns() {

        Table t = new Table();
        new PipesPipeline("a").as("1").step(new StringCharPipe()).as("NO").step(new StringLengthPipe()).as("2").table(t, Arrays.asList("1", "2"), new PipeFunction<String, String>() {
                    public String compute(String a) {
                        return a + "!";
                    }

                }, new PipeFunction<Integer, Integer>() {
            public Integer compute(Integer a) {
                return a + 10;
            }
        }
        ).iterate();

        assertEquals(t.get(0, "1"), "a!");
        assertEquals(t.get(0, "2"), 11);
        assertEquals(t.get(1, "1"), "a!");
        assertEquals(t.get(1, "2"), 12);
        assertEquals(t.getColumnCount(), 2);
        assertEquals(t.getRowCount(), 2);

        t.clear();
        new PipesPipeline("a").as("1").step(new StringCharPipe()).as("NO").step(new StringLengthPipe()).as("2").table(t, Arrays.asList("1", "2"), new PipeFunction<Object, String>() {
            public String compute(Object a) {
                return a.toString() + "!#@";
            }

        }
        ).iterate();

        assertEquals(t.get(0, "1"), "a!#@");
        assertEquals(t.get(0, "2"), "1!#@");
        assertEquals(t.get(1, "1"), "a!#@");
        assertEquals(t.get(1, "2"), "2!#@");
        assertEquals(t.getColumnCount(), 2);
        assertEquals(t.getRowCount(), 2);
    }


    public void testTableColumnWidthOnFailedTraversal() {
        Table t = new Table();
        assertEquals(t.getColumnCount(), -1);
        assertEquals(t.getColumnNames().size(), 0);
        new PipesPipeline("x").as("a").filter(new PipeFunction<Object, Boolean>() {
            public Boolean compute(Object argument) {
                return false;
            }
        }).as("b").table(t).iterate();
        assertEquals(t.getColumnCount(), 2);
        assertEquals(t.getColumnNames().size(), 2);
        assertEquals(t.getColumnNames().get(0), "a");
        assertEquals(t.getColumnNames().get(1), "b");
    }

    private class StringCharPipe extends AbstractPipe<String, String> {
        private String s = null;

        public String processNextStart() {
            if (s != null) {
                String temp = s;
                s = null;
                return temp;
            } else {
                String temp = this.starts.next();
                s = temp + temp;
                return temp;
            }
        }
    }

    private class StringLengthPipe extends AbstractPipe<String, Integer> {
        public Integer processNextStart() {
            return this.starts.next().length();
        }
    }
}
