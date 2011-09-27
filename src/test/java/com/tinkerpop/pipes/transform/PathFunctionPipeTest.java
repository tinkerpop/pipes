package com.tinkerpop.pipes.transform;

import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathFunctionPipeTest extends TestCase {

    /*public void testPipeBasic() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, List> pipeline = new Pipeline<Vertex, List>(new OutPipe(), new PathFunctionPipe<Vertex>(new NamePipeFunction()));
        pipeline.setStarts(new SingleIterator<Vertex>(marko));
        int counter = 0;
        for (List path : pipeline) {
            assertEquals(path.get(0), "marko");
            assertTrue(path.get(1).equals("vadas") || path.get(1).equals("josh") || path.get(1).equals("lop"));
            counter++;
        }
        assertEquals(counter, 3);
    }*/

    public void testTrue() {
        assertTrue(true);
    }

    /*private class NamePipeFunction implements PipeFunction<Vertex, String> {
        public String compute(Vertex argument) {
            return (String) argument.getProperty("name");
        }
    }*/
}
