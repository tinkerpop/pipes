package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.serial.Pipe;
import com.tinkerpop.pipes.serial.Pipeline;
import com.tinkerpop.pipes.serial.pgm.LabelFilterPipe;
import com.tinkerpop.pipes.serial.pgm.PropertyFilterPipe;
import com.tinkerpop.pipes.serial.pgm.VertexEdgePipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrFilterPipeTest extends TestCase {

    public void testOrPipeBasic() {
        List<String> names = Arrays.asList("marko", "povel", "peter", "povel", "marko");
        ObjectFilterPipe<String> pipe1 = new ObjectFilterPipe<String>("marko", ComparisonFilterPipe.Filter.EQUALS);
        ObjectFilterPipe<String> pipe2 = new ObjectFilterPipe<String>("povel", ComparisonFilterPipe.Filter.EQUALS);
        OrFilterPipe<String> orFilterPipe = new OrFilterPipe<String>(pipe1, pipe2);
        orFilterPipe.setStarts(names);
        int counter = 0;
        while (orFilterPipe.hasNext()) {
            String name = orFilterPipe.next();
            assertTrue(name.equals("marko") || name.equals("povel"));
            counter++;
        }
        assertEquals(counter, 4);
    }

    public void testOrPipeGraph() {
        // ./outE[@label='created' or @weight > 0.5]

        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Vertex peter = graph.getVertex("6");
        VertexEdgePipe pipe0 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        LabelFilterPipe pipe1 = new LabelFilterPipe("created", ComparisonFilterPipe.Filter.EQUALS);
        PropertyFilterPipe<Edge, Float> pipe2 = new PropertyFilterPipe<Edge, Float>("weight", 0.5f, ComparisonFilterPipe.Filter.GREATER_THAN);
        OrFilterPipe<Edge> orFilterPipe = new OrFilterPipe<Edge>(pipe1, pipe2);
        Pipeline<Vertex, Edge> pipeline = new Pipeline<Vertex, Edge>(pipe0, orFilterPipe);
        pipeline.setStarts(Arrays.asList(marko, peter, marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            Edge edge = pipeline.next();
            assertTrue(edge.getId().equals("8") || edge.getId().equals("9") || edge.getId().equals("12"));
            assertTrue((Float) edge.getProperty("weight") > 0.5f || edge.getLabel().equals("created"));
            counter++;
        }
        assertEquals(counter, 5);

    }

    public void testAndOrPipeGraph() {
        // ./outE[@label='created' or (@label='knows' and @weight > 0.5)]

        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Edge> pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        FilterPipe<Edge> pipeA = new LabelFilterPipe("created", ComparisonFilterPipe.Filter.EQUALS);
        FilterPipe<Edge> pipeB = new LabelFilterPipe("knows", ComparisonFilterPipe.Filter.EQUALS);
        FilterPipe<Edge> pipeC = new PropertyFilterPipe<Edge, Float>("weight", 0.5f, ComparisonFilterPipe.Filter.GREATER_THAN);
        FilterPipe<Edge> pipeD = new AndFilterPipe<Edge>(pipeB, pipeC);
        FilterPipe<Edge> pipe2 = new OrFilterPipe<Edge>(pipeA, pipeD);
        Pipeline<Vertex, Edge> pipeline = new Pipeline<Vertex, Edge>(pipe1, pipe2);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            Edge edge = pipeline.next();
            assertTrue(edge.getId().equals("8") || edge.getId().equals("9"));
            assertTrue(edge.getLabel().equals("created") || ((Float) edge.getProperty("weight") > 0.5f && edge.getLabel().equals("knows")));
            counter++;
        }
        assertEquals(counter, 2);
    }
}
