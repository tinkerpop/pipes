package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.serial.PipeHelper;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ReadySplitPipeTest extends BaseTest {

    public void testReadySplitPipe() {
        SplitPipe<String> pipe = new ReadySplitPipe<String>(3);
        List<String> starts = BaseTest.generateUUIDs(12);
        pipe.setStarts(starts.iterator());
        List<String> end1 = new ArrayList<String>();
        List<String> end2 = new ArrayList<String>();
        List<String> end3 = new ArrayList<String>();

        Assert.assertTrue(pipe.hasNext());
        Assert.assertTrue(pipe.getSplit(0).hasNext());
        //assertTrue(pipe.getSplit(1).hasNext());
        //assertTrue(pipe.getSplit(2).hasNext());
        PipeHelper.fillCollection(pipe.getSplit(0), end1);
        PipeHelper.fillCollection(pipe.getSplit(1), end2);
        PipeHelper.fillCollection(pipe.getSplit(2), end3);

        Assert.assertEquals(end1.size(), 12);
        Assert.assertEquals(end2.size(), 0);
        Assert.assertEquals(end3.size(), 0);

        for (int i = 0; i < starts.size(); i++) {
            Assert.assertEquals(end1.get(i), starts.get(i));
        }
    }
}
