package com.tinkerpop.pipes.util;

import junit.framework.TestCase;

import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FluentPipelineTest extends TestCase {

    public void testPipeline() {
        System.out.println(new FluentPipeline<String, List, FluentPipeline>("string").aggregate().cap().toList());
    }
}
