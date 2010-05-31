package com.tinkerpop.pipes.merge;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractMergePipe<S> extends AbstractPipe<Iterator<S>, S> implements MergePipe<S> {

}
