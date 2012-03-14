package com.tinkerpop.pipes.util.structures;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TreeTest extends TestCase {

    public void testBasicTree() {
        Tree<String> tree = new Tree<String>();
        tree.put("marko", new Tree<String>(Tree.createTree("a", new Tree<String>("a1", "a2")), Tree.createTree("b", new Tree<String>("b1", "b2", "b3"))));
        tree.put("josh", new Tree<String>("1", "2"));

        assertEquals(tree.getObjectsAtDepth(0).size(), 0);
        assertEquals(tree.getObjectsAtDepth(1).size(), 2);
        assertEquals(tree.getObjectsAtDepth(2).size(), 4);
        assertEquals(tree.getObjectsAtDepth(3).size(), 5);
        assertEquals(tree.getObjectsAtDepth(4).size(), 0);
        assertEquals(tree.getObjectsAtDepth(5).size(), 0);

        assertEquals(tree.get("josh").size(), 2);
        assertEquals(tree.get("marko").get("b").get("b1").size(), 0);
        assertEquals(tree.get("marko").get("b").size(), 3);
        assertNull(tree.get("marko").get("c"));
    }

    public void testTreeLeaves() {
        Tree<String> tree = new Tree<String>();
        tree.put("marko", new Tree<String>(Tree.createTree("a", new Tree<String>("a1", "a2")), Tree.createTree("b", new Tree<String>("b1", "b2", "b3"))));
        tree.put("josh", new Tree<String>("1", "2"));

        assertEquals(tree.getLeafTrees().size(), 7);
        for (Tree<String> t : tree.getLeafTrees()) {
            assertEquals(t.keySet().size(), 1);
            final String key = t.keySet().iterator().next();
            assertTrue(Arrays.asList("a1", "a2", "b1", "b2", "b3", "1", "2").contains(key));
        }

        //System.out.println(tree.getLeafObjects());
        assertEquals(tree.getLeafObjects().size(), 7);
        for (String s : tree.getLeafObjects()) {
            assertTrue(Arrays.asList("a1", "a2", "b1", "b2", "b3", "1", "2").contains(s));
        }
    }
}
