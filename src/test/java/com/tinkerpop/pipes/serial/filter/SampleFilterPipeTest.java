package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.serial.Pipe;
import junit.framework.Assert;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SampleFilterPipeTest extends BaseTest {

    public void testSampleFilterPipe() {
        Pipe<String, String> pipe = new SampleFilterPipe<String>(1.0d);
        pipe.setStarts(BaseTest.generateUUIDs(100).iterator());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        Assert.assertEquals(counter, 100);

        pipe = new SampleFilterPipe<String>(0.0d);
        pipe.setStarts(BaseTest.generateUUIDs(100).iterator());
        counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        Assert.assertEquals(counter, 0);
    }

    public void testSampleFilterPipe5050() {
        Pipe<String, String> pipe = new SampleFilterPipe<String>(0.5d);
        pipe.setStarts(BaseTest.generateUUIDs(1000).iterator());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        //System.out.println(counter);
        Assert.assertTrue(counter > 400 && counter < 600);
    }
}
