package com.tinkerpop.pipes.util.structures;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class Tree<T> extends HashMap<T, Tree<T>> {

    public Tree() {
        super();
    }

    public Tree(final T... children) {
        this();
        for (final T t : children) {
            this.put(t, new Tree<T>());
        }
    }

    public Tree(final Map.Entry<T, Tree<T>>... children) {
        this();
        for (final Map.Entry<T, Tree<T>> entry : children) {
            this.put(entry.getKey(), entry.getValue());
        }
    }


    public List<Tree<T>> getBranches(final int depth) {
        final List<Tree<T>> branches = new LinkedList<Tree<T>>();
        List<Tree<T>> currentDepth = Arrays.asList(this);
        for (int i = 0; i < depth; i++) {
            if (i == depth - 1) {
                return currentDepth;
            } else {
                final List<Tree<T>> temp = new LinkedList<Tree<T>>();
                for (final Tree<T> t : currentDepth) {
                    temp.addAll(t.values());
                }
                currentDepth = temp;
            }
        }
        return branches;
    }

    public List<T> getObjects(final int depth) {
        final List<T> list = new LinkedList<T>();
        for (final Tree<T> t : this.getBranches(depth)) {
            list.addAll(t.keySet());
        }
        return list;
    }

    public static <T> Map.Entry<T, Tree<T>> createTree(T key, Tree<T> tree) {
        return new SimpleEntry<T, Tree<T>>(key, tree);
    }
}
