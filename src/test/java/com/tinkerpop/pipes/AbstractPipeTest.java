package com.tinkerpop.pipes;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerEdge;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerVertex;
import com.tinkerpop.pipes.pgm.EdgeVertexPipe;
import com.tinkerpop.pipes.pgm.PropertyPipe;
import com.tinkerpop.pipes.pgm.VertexEdgePipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AbstractPipeTest extends BaseTest {

    private static final int TOTAL_RUNS = 10;

    public void testIterable() {
        Collection<String> names = Arrays.asList("marko", "josh", "peter");
        Pipe<String, String> pipe = new IdentityPipe<String>();
        pipe.setStarts(names);
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next();
            assertTrue(name.equals("marko") || name.equals("josh") || name.equals("peter"));
        }
        assertEquals(counter, 3);
        pipe.setStarts(names);
        counter = 0;
        for (String name : pipe) {
            assertTrue(name.equals("marko") || name.equals("josh") || name.equals("peter"));
            counter++;
        }
        assertEquals(counter, 3);
    }

    public void testPathConstruction() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Edge> pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Edge, Vertex> pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, String> pipe3 = new PropertyPipe<Vertex, String>("name");
        pipe3.setStarts(pipe2.iterator());
        pipe2.setStarts(pipe1.iterator());
        pipe1.setStarts(Arrays.asList(marko).iterator());

        for (String name : pipe3) {
            List path = pipe3.getPath();
            assertEquals(path.get(0), marko);
            assertEquals(path.get(1).getClass(), TinkerEdge.class);
            assertEquals(path.get(2).getClass(), TinkerVertex.class);
            assertEquals(path.get(3).getClass(), String.class);
            if (name.equals("vadas")) {
                assertEquals(path.get(1), graph.getEdge(7));
                assertEquals(path.get(2), graph.getVertex(2));
                assertEquals(path.get(3), "vadas");
            } else if (name.equals("lop")) {
                assertEquals(path.get(1), graph.getEdge(9));
                assertEquals(path.get(2), graph.getVertex(3));
                assertEquals(path.get(3), "lop");
            } else if (name.equals("josh")) {
                assertEquals(path.get(1), graph.getEdge(8));
                assertEquals(path.get(2), graph.getVertex(4));
                assertEquals(path.get(3), "josh");
            } else {
                assertFalse(true);
            }
            //System.out.println(name);
            //System.out.println(pipeline.getPath());
        }
    }

    /*public void testExceptionTiming() throws Exception {
        Graph graph = new TinkerGraph();
        GraphMLReader.inputGraph(graph, GraphMLReader.class.getResourceAsStream("graph-example-2.xml"));

        double totalTime = 0.0d;
        for (int i = 0; i < TOTAL_RUNS; i++) {
            this.stopWatch();
            int counter = 0;
            for (final Vertex vertex : graph.getVertices()) {
                for (final Edge edge : vertex.getOutEdges()) {
                    final Vertex vertex2 = edge.getInVertex();
                    for (final Edge edge2 : vertex2.getOutEdges()) {
                        final Vertex vertex3 = edge2.getInVertex();
                        for (final Edge edge3 : vertex3.getOutEdges()) {
                            final Vertex vertex4 = edge3.getInVertex();
                            for (final Edge edge4 : vertex4.getOutEdges()) {
                                edge4.getInVertex();
                                counter++;
                            }
                        }
                    }
                }
            }
            double currentTime = this.stopWatch();
            totalTime = totalTime + currentTime;
            BaseTest.printPerformance(graph.toString(), counter, "TinkerGraph vertices reached", currentTime);
        }
        BaseTest.printPerformance("TinkerGraph", 1, "TinkerGraph embedded for looping", totalTime / (double) TOTAL_RUNS);


        totalTime = 0.0d;
        for (int i = 0; i < TOTAL_RUNS; i++) {
            this.stopWatch();
            int counter = 0;
            final Pipe<Vertex, Edge> pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
            final Pipe<Edge, Vertex> pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
            final Pipe<Vertex, Edge> pipe3 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
            final Pipe<Edge, Vertex> pipe4 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
            final Pipe<Vertex, Edge> pipe5 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
            final Pipe<Edge, Vertex> pipe6 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
            final Pipe<Vertex, Edge> pipe7 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
            final Pipe<Edge, Vertex> pipe8 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
            final Pipeline<Vertex, Vertex> pipeline = new Pipeline<Vertex, Vertex>(pipe1, pipe2, pipe3, pipe4, pipe5, pipe6, pipe7, pipe8);
            pipeline.setStarts(graph.getVertices());
            while (pipeline.hasNext()) {
                pipeline.next();
                counter++;
            }
            double currentTime = this.stopWatch();
            totalTime = totalTime + currentTime;
            BaseTest.printPerformance(graph.toString(), counter, "TinkerGraph vertices reached", currentTime);

        }
        BaseTest.printPerformance("TinkerGraph", 1, "TinkerGraph pipes model", totalTime / (double) TOTAL_RUNS);
    }*/
}
