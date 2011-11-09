package com.tinkerpop.pipes.util;

import junit.framework.TestCase;

import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PipesPipelineTest extends TestCase {

    public void testPipeline() {
        System.out.println(new PipesPipeline<String, List>("string").aggregate().cap().toList());
    }

    public void testPipeline2() {
        System.out.println(new PipesPipeline<String, String>("string")._()._().back(2)._().toList());
    }
}
