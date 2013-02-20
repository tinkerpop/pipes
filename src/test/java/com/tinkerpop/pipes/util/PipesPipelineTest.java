package com.tinkerpop.pipes.util;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PipesPipelineTest extends TestCase {

    public void testAsPipesFunctions() {
        List<String> x = new PipesPipeline<String, String>(Arrays.asList("a", "aa", "aaa")).as("x").transform(new PipesFunction() {
            public String compute(Object a) {
                return this.asMap.get("x").toString() + a.toString().length();
            }
        }).toList();

        for (String y : x) {
            assertEquals((Integer) y.substring(0, y.length() - 1).length(), Integer.valueOf(y.substring(y.length() - 1)));
        }
    }
}
