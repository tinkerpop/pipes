package com.tinkerpop.pipes;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerEdge;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerVertex;
import com.tinkerpop.pipes.pgm.EdgeVertexPipe;
import com.tinkerpop.pipes.pgm.PropertyPipe;
import com.tinkerpop.pipes.pgm.VertexEdgePipe;
import com.tinkerpop.pipes.merge.*;
import com.tinkerpop.pipes.split.*;

import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathSequenceTest extends BaseTest {

    private Pipe<Vertex, Vertex> outE_inV(Iterator source) {
        Pipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        Pipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        Pipe<Vertex, Vertex> pipeline = new Pipeline<Vertex, Vertex>(Arrays.asList(pipe1, pipe2));
        pipeline.setStarts(source);
        return pipeline;
    }

    private Pipe<Vertex, Vertex> inE_outV(Iterator source) {
        Pipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.IN_EDGES);
        Pipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.OUT_VERTEX);
        Pipe<Vertex, Vertex> pipeline = new Pipeline<Vertex, Vertex>(Arrays.asList(pipe1, pipe2));
        pipeline.setStarts(source);
        return pipeline;
    }

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
            //System.out.println(path);
        }
    }

    public void testRobinMergePath() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Object> namePipe = outE_inV_Property(marko, "name");
        Pipe<Vertex, Object> agePipe = outE_inV_Property(marko, "age");
        RobinMergePipe<Object> mergePipe = new RobinMergePipe<Object>();
        mergePipe.setStarts(Arrays.asList(namePipe.iterator(), agePipe.iterator()).iterator());
        PathSequence paths = new PathSequence(mergePipe);
        Iterator<String> expected = Arrays.asList(
            "[v[1], e[7][1-knows->2], v[2], vadas]",
            "[v[1], e[7][1-knows->2], v[2], 27]",
            "[v[1], e[9][1-created->3], v[3], lop]",
            "[v[1], e[9][1-created->3], v[3], null]",
            "[v[1], e[8][1-knows->4], v[4], josh]",
            "[v[1], e[8][1-knows->4], v[4], 32]").iterator();
        for (List path : paths) {
            assertEquals(path.toString(), expected.next());
            //System.out.println(path);
        }
    }

    public void testRobinMergeExtendedPath() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Vertex vadas = graph.getVertex("2");
        Pipe<Vertex, Vertex> source1 = outE_inV(Arrays.asList(marko).iterator());
        Pipe<Vertex, Vertex> source2 = inE_outV(Arrays.asList(vadas).iterator());
        RobinMergePipe<Vertex> mergePipe = new RobinMergePipe<Vertex>();
        mergePipe.setStarts(Arrays.asList(source1.iterator(), source2.iterator()).iterator());
        PathSequence paths = new PathSequence(outE_inV(mergePipe.iterator()));
        Iterator<String> expected = Arrays.asList(
            "[v[2], e[7][1-knows->2], v[1], e[7][1-knows->2], v[2]]",
            "[v[2], e[7][1-knows->2], v[1], e[9][1-created->3], v[3]]",
            "[v[2], e[7][1-knows->2], v[1], e[8][1-knows->4], v[4]]",
            "[v[1], e[8][1-knows->4], v[4], e[10][4-created->5], v[5]]",
            "[v[1], e[8][1-knows->4], v[4], e[11][4-created->3], v[3]]").iterator();
        for (List path : paths) {
            assertEquals(path.toString(), expected.next());
            //System.out.println(path);
        }
    }

    public void testCopySplitPath() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Vertex> source = outE_inV(Arrays.asList(marko).iterator());
        SplitPipe<Vertex> split = new CopySplitPipe<Vertex>(2);
        split.setStarts(source.iterator());
        PathSequence paths1 = new PathSequence(split.getSplit(0));
        PathSequence paths2 = new PathSequence(split.getSplit(1));

        List expected = Arrays.asList(
            "[v[1], e[7][1-knows->2], v[2]]",
            "[v[1], e[9][1-created->3], v[3]]",
            "[v[1], e[8][1-knows->4], v[4]]",
            "[v[1], e[7][1-knows->2], v[2]]",
            "[v[1], e[9][1-created->3], v[3]]",
            "[v[1], e[8][1-knows->4], v[4]]"
        );
        Iterator<String> expected1 = expected.iterator();
        for (List path : paths1) {
            //System.out.println(path);
            assertEquals(path.toString(), expected1.next());
        }
        Iterator<String> expected2 = expected.iterator();
        for (List path : paths2) {
            assertEquals(path.toString(), expected2.next());
        }
    }

    public void testCopySplitExtendedPath() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Vertex> source = outE_inV(Arrays.asList(marko).iterator());
        SplitPipe<Vertex> split = new CopySplitPipe<Vertex>(2);
        split.setStarts(source.iterator());
        PathSequence paths1 = new PathSequence(inE_outV(split.getSplit(0)));
        PathSequence paths2 = new PathSequence(inE_outV(split.getSplit(1)));

        List expected = Arrays.asList(
            "[v[1], e[7][1-knows->2], v[2], e[7][1-knows->2], v[1]]",
            "[v[1], e[9][1-created->3], v[3], e[9][1-created->3], v[1]]",
            "[v[1], e[9][1-created->3], v[3], e[11][4-created->3], v[4]]",
            "[v[1], e[9][1-created->3], v[3], e[12][6-created->3], v[6]]",
            "[v[1], e[8][1-knows->4], v[4], e[8][1-knows->4], v[1]]",
            "[v[1], e[7][1-knows->2], v[2], e[7][1-knows->2], v[1]]",
            "[v[1], e[9][1-created->3], v[3], e[9][1-created->3], v[1]]",
            "[v[1], e[9][1-created->3], v[3], e[11][4-created->3], v[4]]",
            "[v[1], e[9][1-created->3], v[3], e[12][6-created->3], v[6]]"
        );
        Iterator<String> expected1 = expected.iterator();
        for (List path : paths1) {
            String e = expected1.next();
            //System.out.println("actual, expected:");
            //System.out.println(path);
            //System.out.println(e);
            assertEquals(path.toString(), e);
        }
        Iterator<String> expected2 = expected.iterator();
        for (List path : paths2) {
            assertEquals(path.toString(), expected2.next());
        }
    }

    public void testCopySplitAndRobinMergePath() {
        System.out.println("public void testCopySplitAndRobinMergePath() {");
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        Pipe<Vertex, Vertex> source = outE_inV(Arrays.asList(marko).iterator());
        SplitPipe<Vertex> split = new CopySplitPipe<Vertex>(2);
        split.setStarts(source.iterator());
        Pipe<Vertex, Vertex> outgoing = outE_inV(split.getSplit(0).iterator());
        Pipe<Vertex, Vertex> incoming = inE_outV(split.getSplit(1).iterator());
        MergePipe<Vertex> merge = new ExhaustiveMergePipe<Vertex>();
        //NOTE: incoming and outgoing are defined in the wrong order here:
        merge.setStarts(Arrays.asList(incoming.iterator(), outgoing.iterator()).iterator());
        PathSequence paths = new PathSequence(merge);

        Iterator<String> expected = Arrays.asList(
            "[v[1], e[7][1-knows->2], v[2], e[7][1-knows->2], v[1]]",
            "[v[1], e[9][1-created->3], v[3], e[9][1-created->3], v[1]]",
            "[v[1], e[9][1-created->3], v[3], e[11][4-created->3], v[4]]",
            "[v[1], e[9][1-created->3], v[3], e[12][6-created->3], v[6]]",
            "[v[1], e[8][1-knows->4], v[4], e[8][1-knows->4], v[1]]",
            "[v[1], e[8][1-knows->4], v[4], e[10][4-created->5], v[5]]",
            "[v[1], e[8][1-knows->4], v[4], e[11][4-created->3], v[3]]"
        ).iterator();
        for (List path : paths) {
            System.out.println(path);
            assertEquals(path.toString(), expected.next());
        }
    }
}
