package com.tinkerpop.pipes;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerEdge;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerVertex;
import com.tinkerpop.pipes.pgm.EdgeVertexPipe;
import com.tinkerpop.pipes.pgm.PropertyPipe;
import com.tinkerpop.pipes.pgm.VertexEdgePipe;
import com.tinkerpop.pipes.merge.ExhaustiveMergePipe;

import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathSequenceTest extends BaseTest {

    private Pipe<Vertex, Object> outE_inV_Property(Vertex vertex, String property) {
        Pipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe pipe3 = new PropertyPipe<Vertex, String>(property);
        Pipe<Vertex, Object> pipeline = new Pipeline<Vertex, Object>(Arrays.asList(pipe1, pipe2, pipe3));
        pipeline.setStarts(Arrays.asList(vertex).iterator());
        return pipeline;
    }

    public void testPathSequencing() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        PathSequence paths = new PathSequence(outE_inV_Property(marko, "name"));

        for (List path : paths) {
            String name = (String) path.get(path.size() - 1);
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
            //System.out.println(path);
        }
    }

    public void testExhaustiveMergePath() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Object> namePipe = outE_inV_Property(marko, "name");
        Pipe<Vertex, Object> agePipe = outE_inV_Property(marko, "age");
        ExhaustiveMergePipe<Object> mergePipe = new ExhaustiveMergePipe<Object>();
        mergePipe.setStarts(Arrays.asList(namePipe.iterator(), agePipe.iterator()).iterator());
        PathSequence paths = new PathSequence(mergePipe);
        Iterator<String> expected = Arrays.asList(
            "[v[1], e[7][1-knows->2], v[2], vadas]",
            "[v[1], e[9][1-created->3], v[3], lop]",
            "[v[1], e[8][1-knows->4], v[4], josh]",
            "[v[1], e[7][1-knows->2], v[2], 27]",
            "[v[1], e[9][1-created->3], v[3], null]",
            "[v[1], e[8][1-knows->4], v[4], 32]").iterator();
        for (List path : paths) {
            assertEquals(path.toString(), expected.next());
            System.out.println(path);
        }
    }
}
