package com.tinkerpop.pipes.merge;

import com.tinkerpop.pipes.BaseTest;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RobinMergePipeTest extends BaseTest {

    public void testRobinMergePipe() {
        RobinMergePipe<String> pipe = new RobinMergePipe<String>();
        List<String> marko = Arrays.asList("marko", "antonio", "rodriguez");
        List<String> peter = Arrays.asList("peter", "neubauer");
        List<String> josh = Arrays.asList("joshua", "shinavier");
        pipe.setStarts(Arrays.asList(marko.iterator(), peter.iterator(), josh.iterator()).iterator());
        Assert.assertTrue(pipe.hasNext());
        int counter = 0;
        List<String> ends = new ArrayList<String>();
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next();
            //System.out.println(name);
            Assert.assertTrue(marko.contains(name) || peter.contains(name) || josh.contains(name));
            ends.add(name);
        }
        Assert.assertEquals(counter, 7);
        Assert.assertEquals(counter, ends.size());
        Assert.assertEquals(ends.get(0), "marko");
        Assert.assertEquals(ends.get(1), "peter");
        Assert.assertEquals(ends.get(2), "joshua");
        Assert.assertEquals(ends.get(3), "antonio");
        Assert.assertEquals(ends.get(4), "neubauer");
        Assert.assertEquals(ends.get(5), "shinavier");
        Assert.assertEquals(ends.get(6), "rodriguez");
    }

    public void testRobinMergePipeLastEmpty() {
        RobinMergePipe<String> pipe = new RobinMergePipe<String>();
        List<String> marko = Arrays.asList("marko");
        List<String> josh = Arrays.asList("joshua", "shinavier");
        List<String> peter = Arrays.asList();
        pipe.setStarts(Arrays.asList(marko.iterator(), josh.iterator(), peter.iterator()).iterator());
        Assert.assertTrue(pipe.hasNext());
        int counter = 0;
        List<String> ends = new ArrayList<String>();
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next();
            Assert.assertTrue(marko.contains(name) || peter.contains(name) || josh.contains(name));
            ends.add(name);
        }
        Assert.assertEquals(counter, 3);
        Assert.assertEquals(counter, ends.size());
        Assert.assertEquals(ends.get(0), "marko");
        Assert.assertEquals(ends.get(1), "joshua");
        Assert.assertEquals(ends.get(2), "shinavier");
    }

    public void testRobinMergePipeSomeEmpty() {
        RobinMergePipe<String> pipe = new RobinMergePipe<String>();
        List<String> marko = Arrays.asList("marko");
        List<String> peter = Arrays.asList();
        List<String> josh = Arrays.asList("joshua", "shinavier");
        pipe.setStarts(Arrays.asList(marko.iterator(), peter.iterator(), josh.iterator()).iterator());
        Assert.assertTrue(pipe.hasNext());
        int counter = 0;
        List<String> ends = new ArrayList<String>();
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next();
            Assert.assertTrue(marko.contains(name) || peter.contains(name) || josh.contains(name));
            ends.add(name);
        }
        Assert.assertEquals(counter, 3);
        Assert.assertEquals(counter, ends.size());
        Assert.assertEquals(ends.get(0), "marko");
        Assert.assertEquals(ends.get(1), "joshua");
        Assert.assertEquals(ends.get(2), "shinavier");
    }

    public void testRobinMergePipeEmpty() {
        RobinMergePipe<String> pipe = new RobinMergePipe<String>();
        List<String> marko = Arrays.asList();
        List<String> peter = Arrays.asList();
        List<String> josh = Arrays.asList();
        pipe.setStarts(Arrays.asList(marko.iterator(), peter.iterator(), josh.iterator()).iterator());
        Assert.assertFalse(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        Assert.assertEquals(counter, 0);
    }

    public void testRobinMergePipeSingle() {
        RobinMergePipe<String> pipe = new RobinMergePipe<String>();
        List<String> marko = Arrays.asList("marko", "antonio", "rodriguez");
        pipe.setStarts(Arrays.asList(marko.iterator()).iterator());
        Assert.assertTrue(pipe.hasNext());
        int counter = 0;
        List<String> ends = new ArrayList<String>();
        while (pipe.hasNext()) {
            counter++;
            String name = pipe.next();
            Assert.assertTrue(marko.contains(name));
            ends.add(name);
        }
        Assert.assertEquals(counter, 3);
        Assert.assertEquals(counter, ends.size());
        Assert.assertEquals(ends.get(0), "marko");
        Assert.assertEquals(ends.get(1), "antonio");
        Assert.assertEquals(ends.get(2), "rodriguez");
    }

    public void testRobinMergePipeSingleEmpty() {
        RobinMergePipe<String> pipe = new RobinMergePipe<String>();
        List<String> marko = Arrays.asList();
        pipe.setStarts(Arrays.asList(marko.iterator()).iterator());
        Assert.assertFalse(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();

        }
        Assert.assertEquals(counter, 0);
    }


}
