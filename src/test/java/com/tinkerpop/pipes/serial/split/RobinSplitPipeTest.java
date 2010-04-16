package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.serial.PipeHelper;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RobinSplitPipeTest extends BaseTest {

    public void testRobinSplitPipeOrdering() {
        SplitPipe<String> pipe = new RobinSplitPipe<String>(3);
        List<String> starts = BaseTest.generateUUIDs(12);
        pipe.setStarts(starts.iterator());
        List<String> end1 = new ArrayList<String>();
        List<String> end2 = new ArrayList<String>();
        List<String> end3 = new ArrayList<String>();

        PipeHelper.fillCollection(pipe.getSplit(0), end1);
        PipeHelper.fillCollection(pipe.getSplit(1), end2);
        PipeHelper.fillCollection(pipe.getSplit(2), end3);

        Assert.assertEquals(end1.size(), 4);
        Assert.assertEquals(end2.size(), 4);
        Assert.assertEquals(end3.size(), 4);

        Assert.assertEquals(end1.get(0), starts.get(0));
        Assert.assertEquals(end1.get(1), starts.get(3));
        Assert.assertEquals(end1.get(2), starts.get(6));
        Assert.assertEquals(end1.get(3), starts.get(9));
        Assert.assertEquals(end2.get(0), starts.get(1));
        Assert.assertEquals(end2.get(1), starts.get(4));
        Assert.assertEquals(end2.get(2), starts.get(7));
        Assert.assertEquals(end2.get(3), starts.get(10));
        Assert.assertEquals(end3.get(0), starts.get(2));
        Assert.assertEquals(end3.get(1), starts.get(5));
        Assert.assertEquals(end3.get(2), starts.get(8));
        Assert.assertEquals(end3.get(3), starts.get(11));
    }

    public void testRobinSplitPipeSize() {
        SplitPipe<String> pipe = new RobinSplitPipe<String>(5);
        List<String> starts = BaseTest.generateUUIDs(101);
        pipe.setStarts(starts.iterator());
        List<String> end1 = new ArrayList<String>();
        List<String> end2 = new ArrayList<String>();
        List<String> end3 = new ArrayList<String>();
        List<String> end4 = new ArrayList<String>();
        List<String> end5 = new ArrayList<String>();

        PipeHelper.fillCollection(pipe.getSplit(0), end1);
        PipeHelper.fillCollection(pipe.getSplit(1), end2);
        PipeHelper.fillCollection(pipe.getSplit(2), end3);
        PipeHelper.fillCollection(pipe.getSplit(3), end4);
        PipeHelper.fillCollection(pipe.getSplit(4), end5);

        Assert.assertEquals(end1.size(), 21);
        Assert.assertEquals(end2.size(), 20);
        Assert.assertEquals(end3.size(), 20);
        Assert.assertEquals(end4.size(), 20);
        Assert.assertEquals(end5.size(), 20);
    }

    public void testRobinSplitPipeSmall() {
        SplitPipe<String> pipe = new RobinSplitPipe<String>(3);
        List<String> starts = BaseTest.generateUUIDs(2);
        pipe.setStarts(starts.iterator());
        List<String> end1 = new ArrayList<String>();
        List<String> end2 = new ArrayList<String>();
        List<String> end3 = new ArrayList<String>();

        Assert.assertTrue(pipe.getSplit(0).hasNext());
        Assert.assertTrue(pipe.getSplit(1).hasNext());
        Assert.assertFalse(pipe.getSplit(2).hasNext());

        PipeHelper.fillCollection(pipe.getSplit(0), end1);
        PipeHelper.fillCollection(pipe.getSplit(1), end2);
        PipeHelper.fillCollection(pipe.getSplit(2), end3);

        Assert.assertEquals(end1.size(), 1);
        Assert.assertEquals(end2.size(), 1);
        Assert.assertEquals(end3.size(), 0);
    }

    public void testRobinSplitPipeEmpty() {
        SplitPipe<String> pipe = new RobinSplitPipe<String>(3);
        List<String> starts = BaseTest.generateUUIDs(0);
        pipe.setStarts(starts.iterator());
        List<String> end1 = new ArrayList<String>();
        List<String> end2 = new ArrayList<String>();
        List<String> end3 = new ArrayList<String>();

        Assert.assertFalse(pipe.getSplit(0).hasNext());
        Assert.assertFalse(pipe.getSplit(1).hasNext());
        Assert.assertFalse(pipe.getSplit(2).hasNext());

        PipeHelper.fillCollection(pipe.getSplit(0), end1);
        PipeHelper.fillCollection(pipe.getSplit(1), end2);
        PipeHelper.fillCollection(pipe.getSplit(2), end3);

        Assert.assertEquals(end1.size(), 0);
        Assert.assertEquals(end2.size(), 0);
        Assert.assertEquals(end3.size(), 0);

    }
}
