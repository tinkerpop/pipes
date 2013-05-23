package com.tinkerpop.pipes.transform;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Query;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GraphQueryPipeTest extends TestCase {

    private final Graph graph = TinkerGraphFactory.createTinkerGraph();

    public void testDegenerateCase() {
        GraphQueryPipe<Vertex> pipe = new GraphQueryPipe<Vertex>(Vertex.class);
        pipe.setStarts(Arrays.asList(graph));
        int counter = 0;
        while (pipe.hasNext()) {
            pipe.next();
            counter++;
        }
        assertEquals(counter, 6);

    }

    public void testQueryHas() {
        GraphQueryPipe<Vertex> pipe = new GraphQueryPipe<Vertex>(Vertex.class);
        pipe.setStarts(Arrays.asList(graph));
        pipe.addHasContainer(new QueryPipe.HasContainer("name", Query.Compare.EQUAL, "marko"));
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next().getProperty("name"), "marko");
            counter++;
        }
        assertEquals(counter, 1);

        pipe.reset();

        pipe.setStarts(Arrays.asList(graph));
        pipe.addHasContainer(new QueryPipe.HasContainer("name", Query.Compare.EQUAL, "marko"));
        pipe.addHasContainer(new QueryPipe.HasContainer("name", Query.Compare.NOT_EQUAL, "bobby"));
        counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next().getProperty("name"), "marko");
            counter++;
        }
        assertEquals(counter, 1);

        pipe.reset();

        pipe.setStarts(Arrays.asList(graph));
        pipe.addHasContainer(new QueryPipe.HasContainer("name", Query.Compare.EQUAL, "marko"));
        pipe.addHasContainer(new QueryPipe.HasContainer("name", Query.Compare.EQUAL, "bobby"));
        assertFalse(pipe.hasNext());

        pipe.reset();

        pipe.setStarts(Arrays.asList(graph));
        pipe.addHasContainer(new QueryPipe.HasContainer("name", Query.Compare.EQUAL, "marko"));
        pipe.setHighRange(0);
        assertFalse(pipe.hasNext());
    }

    public void testQueryLimit() {
        GraphQueryPipe<Vertex> pipe = new GraphQueryPipe<Vertex>(Vertex.class);
        pipe.setHighRange(2);
        pipe.setStarts(Arrays.asList(graph));
        int counter = 0;
        while (pipe.hasNext()) {
            pipe.next();
            counter++;
        }
        assertEquals(counter, 3);

        pipe.reset();

        pipe.setHighRange(3);
        pipe.setStarts(Arrays.asList(graph));
        counter = 0;
        while (pipe.hasNext()) {
            pipe.next();
            counter++;
        }
        assertEquals(counter, 4);


    }
}
