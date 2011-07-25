package com.tinkerpop.pipes.filter;

import junit.framework.TestCase;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AbstractComparisonFilterPipeTest extends TestCase {

    public void testComparisons() {
        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.EQUAL).compareObjects(1, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.NOT_EQUAL).compareObjects(1, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.EQUAL).compareObjects(1, 2));
        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.NOT_EQUAL).compareObjects(1, 2));

        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN).compareObjects(2, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN).compareObjects(2, 2));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN).compareObjects(2, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN).compareObjects(2, 2));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN).compareObjects(1, 2));
        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN).compareObjects(1, 2));

        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN_EQUAL).compareObjects(2, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN_EQUAL).compareObjects(2, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN_EQUAL).compareObjects(1, 2));
        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN_EQUAL).compareObjects(1, 2));

        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN_EQUAL).compareObjects(1, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN_EQUAL).compareObjects(2, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN_EQUAL).compareObjects(1, 2));
        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN_EQUAL).compareObjects(1, 1));

        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.EQUAL).compareObjects(null, null));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.NOT_EQUAL).compareObjects(null, null));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.EQUAL).compareObjects(1, null));
        assertTrue(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.NOT_EQUAL).compareObjects(1, null));

        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN).compareObjects(1, null));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN).compareObjects(null, 1));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.LESS_THAN_EQUAL).compareObjects(1, null));
        assertFalse(new BasicComparisonFilterPipe(ComparisonFilterPipe.Filter.GREATER_THAN_EQUAL).compareObjects(null, 1));
    }

    public class BasicComparisonFilterPipe extends AbstractComparisonFilterPipe<Object, Object> {

        public BasicComparisonFilterPipe(Filter filter) {
            super(filter);
        }

        public Object processNextStart() {
            throw new UnsupportedOperationException();
        }
    }
}