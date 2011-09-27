package com.tinkerpop.pipes.filter;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AndFilterPipeTest extends TestCase {

    public void testAndPipeBasic() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "povel", "marko");
        ObjectFilterPipe<String> pipe1 = new ObjectFilterPipe<String>("marko", FilterPipe.Filter.EQUAL);
        ObjectFilterPipe<String> pipe2 = new ObjectFilterPipe<String>("povel", FilterPipe.Filter.EQUAL);
        AndFilterPipe<String> andFilterPipe = new AndFilterPipe<String>(pipe1, pipe2);
        andFilterPipe.setStarts(names);
        int counter = 0;
        while (andFilterPipe.hasNext()) {
            String name = andFilterPipe.next();
            //System.out.println(name);
            counter++;
        }
        assertEquals(counter, 0);
    }

    /*public void testAndPipeGraph() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Vertex peter = graph.getVertex("6");
        OutEdgesPipe pipe0 = new OutEdgesPipe();
        LabelFilterPipe pipe1 = new LabelFilterPipe("knows", FilterPipe.Filter.EQUAL);
        PropertyFilterPipe<Edge, Float> pipe2 = new PropertyFilterPipe<Edge, Float>("weight", 0.5f, FilterPipe.Filter.GREATER_THAN);
        AndFilterPipe<Edge> andFilterPipe = new AndFilterPipe<Edge>(pipe1, pipe2);
        Pipeline<Vertex, Edge> pipeline = new Pipeline<Vertex, Edge>(pipe0, andFilterPipe);
        pipeline.setStarts(Arrays.asList(marko, peter, marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            Edge edge = pipeline.next();
            assertTrue(edge.getId().equals("8"));
            assertTrue((Float) edge.getProperty("weight") > 0.5f && edge.getLabel().equals("knows"));
            counter++;
        }
        assertEquals(counter, 2);

    }*/

}
