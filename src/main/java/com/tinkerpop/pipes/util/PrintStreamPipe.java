package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.AbstractPipe;

import java.io.IOException;
import java.io.PrintStream;

/**
 * The PrintStreamPipe emits its consumed objects unchanged.
 * However, for each object that passes through the pipe, the printMethod() is called.
 * The standard implementation of the printMethod() is to simply println() the passing object to the provided PrintStream.
 * Override printMethod() to effect different printing behavior.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PrintStreamPipe<S> extends AbstractPipe<S, S> {

    private final PrintStream printStream;

    public PrintStreamPipe(final PrintStream printStream) {
        this.printStream = printStream;
    }

    public void closePrintStream() throws IOException {
        this.printStream.close();
    }

    protected S processNextStart() {
        S s = this.starts.next();
        this.printMethod(s);
        return s;
    }

    public PrintStream getPrintStream() {
        return this.printStream;
    }

    public void printMethod(final S s) {
        this.printStream.println(s);
    }
}
