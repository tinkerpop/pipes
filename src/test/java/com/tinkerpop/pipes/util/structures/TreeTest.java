package com.tinkerpop.pipes.util.structures;

import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TreeTest extends TestCase {

    public void testBasicTree() {
        Tree<String> tree = new Tree<String>();
        tree.put("marko", new Tree<String>(Tree.createTree("a", new Tree<String>("a1", "a2")), Tree.createTree("b", new Tree<String>("b1", "b2", "b3"))));
        tree.put("josh", new Tree<String>("1", "2"));

        assertEquals(tree.getObjects(0).size(), 0);
        assertEquals(tree.getObjects(1).size(), 2);
        assertEquals(tree.getObjects(2).size(), 4);
        assertEquals(tree.getObjects(3).size(), 5);
        assertEquals(tree.getObjects(4).size(), 0);
        assertEquals(tree.getObjects(5).size(), 0);

        assertEquals(tree.get("josh").size(), 2);
        assertEquals(tree.get("marko").get("b").get("b1").size(), 0);
        assertEquals(tree.get("marko").get("b").size(), 3);
        assertNull(tree.get("marko").get("c"));

    }
}
