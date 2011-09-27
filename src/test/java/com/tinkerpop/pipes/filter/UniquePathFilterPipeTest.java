package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class UniquePathFilterPipeTest extends TestCase {

    public void testPipeBasic() {
        Pipe pipe = new Pipeline(new CharacterCountPipe(), new CharacterCountPipe(), new UniquePathFilterPipe());
        pipe.setStarts(Arrays.asList("1", "11"));
        int counter = 0;
        while (pipe.hasNext()) {
            assertEquals(pipe.next(), "1");
            counter++;
        }
        assertEquals(counter, 1);
    }

    private class CharacterCountPipe extends AbstractPipe<String, String> {
        protected String processNextStart() {
            return "" + this.starts.next().length();
        }
    }

}
