package com.tinkerpop.pipes;

/**
 * A PipeFunction is a function that is passed into certain Pipes that augments the computation that the Pipe evaluates.
 * The A type is the argument type of the compute() method.
 * The B type is the return type of the compute() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface PipeFunction<A, B> {

    /**
     * Compute the function on A to return B.
     * @param argument An argument of type A
     * @return the result of computing the function on the parameter
     */
    public B compute(A argument);
}
