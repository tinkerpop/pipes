package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.Pipe;
import junit.framework.Assert;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RandomFilterPipeTest extends BaseTest {

    public void testPipeBasic() {
        Pipe<String, String> pipe = new RandomFilterPipe<String>(1.0d);
        pipe.setStarts(BaseTest.generateUUIDs(100).iterator());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        Assert.assertEquals(counter, 100);

        pipe = new RandomFilterPipe<String>(0.0d);
        pipe.setStarts(BaseTest.generateUUIDs(100).iterator());
        counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        Assert.assertEquals(counter, 0);
    }

    public void testRandomFilterPipe5050() {
        Pipe<String, String> pipe = new RandomFilterPipe<String>(0.5d);
        pipe.setStarts(BaseTest.generateUUIDs(1000).iterator());
        int counter = 0;
        while (pipe.hasNext()) {
            counter++;
            pipe.next();
        }
        Assert.assertTrue(counter > 400 && counter < 600);
    }
}
