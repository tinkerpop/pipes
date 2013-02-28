package com.tinkerpop.pipes.util;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AsPipeTest extends TestCase {

    public void testAsPipe() {
        List<String> strings = new PipesPipeline<String, String>(Arrays.asList("1", "2", "3")).as("x")._().transform(new PipesFunction<String, String>() {
            @Override
            public String compute(String argument) {
                return this.asMap.get("x") + argument;
            }
        }).toList();

        assertEquals(strings.get(0), "11");
        assertEquals(strings.get(1), "22");
        assertEquals(strings.get(2), "33");
    }
}
