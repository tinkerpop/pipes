package com.tinkerpop.pipes.serial.merge;


import com.tinkerpop.pipes.BaseTest;
import com.tinkerpop.pipes.serial.Pipe;
import com.tinkerpop.pipes.serial.WasteTimePipe;
import junit.framework.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ReadyMergePipeTest extends BaseTest {

    public void testReadyMergePipeSomeEmpty() {
        List<String> starts1 = BaseTest.generateUUIDs("1-", 0);
        List<String> starts2 = BaseTest.generateUUIDs("2-", 10);
        List<String> starts3 = BaseTest.generateUUIDs("3-", 0);
        Pipe<String, String> pipe1 = new WasteTimePipe();
        Pipe<String, String> pipe2 = new WasteTimePipe();
        Pipe<String, String> pipe3 = new WasteTimePipe();
        pipe1.setStarts(starts1.iterator());
        pipe2.setStarts(starts2.iterator());
        pipe3.setStarts(starts3.iterator());
        this.stopWatch();
        ReadyMergePipe readyMergePipe = new ReadyMergePipe<String>();
        readyMergePipe.setStarts(Arrays.asList(pipe1, pipe2, pipe3).iterator());
        int counter = 0;
        Assert.assertTrue(readyMergePipe.hasNext());
        while (readyMergePipe.hasNext()) {
            //System.out.println(readyMergePipe.next());
            counter++;
            readyMergePipe.next();
        }
        Assert.assertEquals(counter, 10);
    }

    public void testReadyMergePipeEmpty() {
        List<String> starts1 = BaseTest.generateUUIDs("1-", 0);
        List<String> starts2 = BaseTest.generateUUIDs("2-", 0);
        List<String> starts3 = BaseTest.generateUUIDs("3-", 0);
        Pipe<String, String> pipe1 = new WasteTimePipe();
        Pipe<String, String> pipe2 = new WasteTimePipe();
        Pipe<String, String> pipe3 = new WasteTimePipe();
        pipe1.setStarts(starts1.iterator());
        pipe2.setStarts(starts2.iterator());
        pipe3.setStarts(starts3.iterator());
        this.stopWatch();
        ReadyMergePipe readyMergePipe = new ReadyMergePipe<String>();
        readyMergePipe.setStarts(Arrays.asList(pipe1, pipe2, pipe3).iterator());
        int counter = 0;
        Assert.assertFalse(readyMergePipe.hasNext());
        while (readyMergePipe.hasNext()) {
            //System.out.println(readyMergePipe.next());
            counter++;
            readyMergePipe.next();
        }
        Assert.assertEquals(counter, 0);
    }

    public void testReadyMergePipeTiming() {
        List<String> starts1 = BaseTest.generateUUIDs("1-", 100);
        List<String> starts2 = BaseTest.generateUUIDs("2-", 100);
        List<String> starts3 = BaseTest.generateUUIDs("3-", 100);
        Pipe<String, String> pipe1 = new WasteTimePipe();
        Pipe<String, String> pipe2 = new WasteTimePipe();
        Pipe<String, String> pipe3 = new WasteTimePipe();
        pipe1.setStarts(starts1.iterator());
        pipe2.setStarts(starts2.iterator());
        pipe3.setStarts(starts3.iterator());
        this.stopWatch();
        ReadyMergePipe readyMergePipe = new ReadyMergePipe<String>();
        readyMergePipe.setStarts(Arrays.asList(pipe1, pipe2, pipe3).iterator());
        int counter = 0;
        Assert.assertTrue(readyMergePipe.hasNext());
        while (readyMergePipe.hasNext()) {
            //System.out.println(readyMergePipe.next());
            counter++;
            readyMergePipe.next();
        }
        Assert.assertEquals(counter, 300);
        BaseTest.printPerformance("Ready merge", 300, "elements over 3 pipes", this.stopWatch());

        pipe1 = new WasteTimePipe();
        pipe2 = new WasteTimePipe();
        pipe3 = new WasteTimePipe();
        pipe1.setStarts(starts1.iterator());
        pipe2.setStarts(starts2.iterator());
        pipe3.setStarts(starts3.iterator());
        this.stopWatch();
        RobinMergePipe robinMergePipe = new RobinMergePipe<String>();
        robinMergePipe.setStarts(Arrays.asList(pipe1, pipe2, pipe3).iterator());
        counter = 0;
        Assert.assertTrue(robinMergePipe.hasNext());
        while (robinMergePipe.hasNext()) {
            //System.out.println(robinMergePipe.next());
            counter++;
            robinMergePipe.next();
        }
        Assert.assertEquals(counter, 300);
        BaseTest.printPerformance("Round robin merge", 300, "elements over 3 pipes", this.stopWatch());

        pipe1 = new WasteTimePipe();
        pipe1.setStarts(BaseTest.generateUUIDs("1-", 300).iterator());
        this.stopWatch();
        counter = 0;
        Assert.assertTrue(pipe1.hasNext());
        while (pipe1.hasNext()) {
            //System.out.println(robinMergePipe.next());
            counter++;
            pipe1.next();
        }
        Assert.assertEquals(counter, 300);
        BaseTest.printPerformance("No merge", 300, "elements over 1 pipe", this.stopWatch());
    }
}
