package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.serial.PipeHelper;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CopySplitPipeTest extends BaseTest {

    public void testCopySplitPipe() {
        SplitPipe<String> pipe = new CopySplitPipe<String>(3);
        List<String> starts = BaseTest.generateUUIDs(12);
        pipe.setStarts(starts.iterator());
        List<String> end1 = new ArrayList<String>();
        List<String> end2 = new ArrayList<String>();
        List<String> end3 = new ArrayList<String>();

        Assert.assertTrue(pipe.getSplit(0).hasNext());
        Assert.assertTrue(pipe.getSplit(1).hasNext());
        Assert.assertTrue(pipe.getSplit(2).hasNext());

        PipeHelper.fillCollection(pipe.getSplit(0), end1);
        PipeHelper.fillCollection(pipe.getSplit(1), end2);
        PipeHelper.fillCollection(pipe.getSplit(2), end3);

        Assert.assertEquals(end1.size(), 12);
        Assert.assertEquals(end2.size(), 12);
        Assert.assertEquals(end3.size(), 12);

        for (int i = 0; i < 12; i++) {
            Assert.assertEquals(starts.get(i), end1.get(i));
            Assert.assertEquals(end1.get(i), end2.get(i));
            Assert.assertEquals(end2.get(i), end3.get(i));
        }
    }

    public void testCopySplitPipeEmpty() {
        SplitPipe<String> pipe = new CopySplitPipe<String>(3);
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

    public void testBadSplit() {
        SplitPipe<String> pipe = new CopySplitPipe<String>(0);
        List<String> starts = BaseTest.generateUUIDs(10);
        pipe.setStarts(starts.iterator());
        try {
            Assert.assertFalse(pipe.getSplit(0).hasNext());
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
}
