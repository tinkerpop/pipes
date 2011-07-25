package com.tinkerpop.pipes;

/**
 * A PipeClosure is a function that is passed into certain Pipes that augment the computation that the Pipe evaluates.
 * The T type is the return type of the compute() method.
 * The P type is the class of the hosting pipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface PipeClosure<T, P extends Pipe> {

    /**
     * @param parameters An array of parameters passed in from the host pipe. Implementation specifics determine what the types of the parameters are.
     * @return the result of computing a function on the parameters
     */
    public T compute(final Object... parameters);

    /**
     * The host pipe for which the PipeClosure is closing.
     * This is a useful reference when needing methods of the host pipe during the computation.
     *
     * @param hostPipe the host pipe
     */
    public void setPipe(final P hostPipe);
}
