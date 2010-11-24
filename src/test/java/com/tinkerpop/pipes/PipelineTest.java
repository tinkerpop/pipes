package com.tinkerpop.pipes;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerEdge;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerVertex;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;
import com.tinkerpop.pipes.pgm.EdgeVertexPipe;
import com.tinkerpop.pipes.pgm.LabelFilterPipe;
import com.tinkerpop.pipes.pgm.PropertyPipe;
import com.tinkerpop.pipes.pgm.VertexEdgePipe;
import junit.framework.TestCase;

import java.util.*;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PipelineTest extends TestCase {

    public void testOneStagePipeline() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe vep = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe<Vertex, Edge> pipeline = new Pipeline<Vertex, Edge>(Arrays.asList(vep));
        pipeline.setStarts(Arrays.asList(marko).iterator());
        assertTrue(pipeline.hasNext());
        int counter = 0;
        Iterator<Vertex> expectedEnds = Arrays.asList(
            graph.getVertex("2"),
            graph.getVertex("3"),
            graph.getVertex("4")).iterator();
        Iterator<String> expectedPaths = Arrays.asList(
            "[v[1], e[7][1-knows->2]]",
            "[v[1], e[9][1-created->3]]",
            "[v[1], e[8][1-knows->4]]").iterator();
        pipeline.enablePath();
        while (pipeline.hasNext()) {
            Edge e = pipeline.next();
            assertEquals(expectedEnds.next(), e.getInVertex());
            List path = pipeline.getPath();
            assertEquals(expectedPaths.next(), path.toString());
            counter++;
        }
        assertEquals(3, counter);
    }

    public void testThreeStagePipeline() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe vep = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe lfp = new LabelFilterPipe("created", ComparisonFilterPipe.Filter.NOT_EQUAL);
        Pipe evp = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, Vertex> pipeline = new Pipeline<Vertex, Vertex>(Arrays.asList(vep, lfp, evp));
        pipeline.setStarts(Arrays.asList(marko).iterator());
        assertTrue(pipeline.hasNext());
        int counter = 0;
        pipeline.enablePath();
        while (pipeline.hasNext()) {
            assertEquals(pipeline.next().getId(), "3");
            List path = pipeline.getPath();
            if (counter == 0) {
                assertEquals(path, Arrays.asList(graph.getVertex("1"), graph.getEdge(9), graph.getVertex(3)));
            }
            counter++;
        }
        assertEquals(1, counter);

        vep = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        lfp = new LabelFilterPipe("created", ComparisonFilterPipe.Filter.EQUAL);
        evp = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        pipeline = new Pipeline<Vertex, Vertex>(vep, lfp, evp);
        pipeline.setStarts(Arrays.asList(marko).iterator());
        assertTrue(pipeline.hasNext());
        counter = 0;
        while (pipeline.hasNext()) {
            Vertex v = pipeline.next();
            assertTrue(v.getId().equals("4") || v.getId().equals("2"));
            counter++;
        }
        assertEquals(2, counter);
        try {
            pipeline.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }

    }

    public void testPipelineResuse() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe vep = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe evp = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, Vertex> pipeline = new Pipeline<Vertex, Vertex>(Arrays.asList(vep, evp));
        pipeline.setStarts(Arrays.asList(marko).iterator());
        assertTrue(pipeline.hasNext());
        int counter = 0;
        if (pipeline.hasNext()) {
            counter++;
            pipeline.next();
        }
        assertEquals(1, counter);

        pipeline.setStarts(Arrays.asList(marko).iterator());
        assertTrue(pipeline.hasNext());
        counter = 0;
        while (pipeline.hasNext()) {
            counter++;
            pipeline.next();
        }
        assertEquals(5, counter);
        try {
            pipeline.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
    }

    public void testTwoSimilarConstructions() {
        List<String> names = Arrays.asList("marko", "peter", "josh");
        IdentityPipe<String> pipe1 = new IdentityPipe<String>();
        IdentityPipe<String> pipe2 = new IdentityPipe<String>();
        Pipeline<String, String> pipeline = new Pipeline<String, String>(pipe1, pipe2);
        pipeline.setStarts(names);
        int counter = 0;
        for (String name : pipeline) {
            counter++;
            assertTrue(name.equals("marko") || name.equals("peter") || name.equals("josh"));
        }
        assertEquals(counter, 3);

        pipe1 = new IdentityPipe<String>();
        pipe2 = new IdentityPipe<String>();
        pipeline = new Pipeline<String, String>();
        pipeline.setStartPipe(pipe1);
        pipeline.setEndPipe(pipe2);
        // when only setting starts and ends, the intermediate pipes must be chained manually.
        pipe2.setStarts((Iterator<String>) pipe1);
        pipeline.setStarts(names);
        counter = 0;
        for (String name : pipeline) {
            counter++;
            assertTrue(name.equals("marko") || name.equals("peter") || name.equals("josh"));
        }
        assertEquals(counter, 3);


    }

    public void testPipelinePathConstruction() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe pipe3 = new PropertyPipe<Vertex, String>("name");
        Pipe<Vertex, String> pipeline = new Pipeline<Vertex, String>(Arrays.asList(pipe1, pipe2, pipe3));
        pipeline.setStarts(Arrays.asList(marko).iterator());
        pipeline.enablePath();

        for (String name : pipeline) {
            List path = pipeline.getPath();
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

    public void testPathWithPipelineOfPipelines() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe pipe3 = new PropertyPipe<Vertex, String>("name");
        Pipe<Vertex, Edge> pipeline1 = new Pipeline<Vertex, Edge>(pipe1);
        Pipe<Edge, Vertex> pipeline2 = new Pipeline<Edge, Vertex>(pipe2);
        Pipe<Vertex, String> pipeline = new Pipeline<Vertex, String>(pipeline1, pipeline2, pipe3);
        pipeline.setStarts(Arrays.asList(marko).iterator());
        pipeline.enablePath();

        for (String name : pipeline) {
            List path = pipeline.getPath();
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
        }
    }

}
