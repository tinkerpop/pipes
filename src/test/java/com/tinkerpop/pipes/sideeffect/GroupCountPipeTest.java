package com.tinkerpop.pipes.sideeffect;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GroupCountPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "josh", "peter", "peter", "peter", "josh");
        SideEffectPipe<String, Map<String, Number>> pipe = new GroupCountPipe<String>();
        pipe.setStarts(names);
        assertTrue(pipe.hasNext());
        int counter = 0;
        for (String name : pipe) {
            assertTrue(name.equals("marko") || name.equals("josh") || name.equals("peter"));
            counter++;
        }
        assertEquals(counter, 6);
        assertEquals(pipe.getSideEffect().get("marko"), new Long(1));
        assertEquals(pipe.getSideEffect().get("josh"), new Long(2));
        assertEquals(pipe.getSideEffect().get("peter"), new Long(3));
        assertNull(pipe.getSideEffect().get("povel"));
        assertFalse(pipe.hasNext());

        pipe.reset();
        assertEquals(0, pipe.getSideEffect().size());
    }

    public void testPipeNoElements() {
        List<String> names = Arrays.asList();
        SideEffectPipe<String, Map<String, Number>> pipe = new GroupCountPipe<String>();
        pipe.setStarts(names);
        assertFalse(pipe.hasNext());
        assertNull(pipe.getSideEffect().get("povel"));
    }
}
