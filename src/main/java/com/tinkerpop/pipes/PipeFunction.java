package com.tinkerpop.pipes;

/**
 * A PipeFunction is a function that is passed into certain pipes in order to augment the computation of that pipe.
 * The A type is the argument type of the compute() method.
 * The B type is the return type of the compute() method.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @author Zach Cox (http://theza.ch)
 */
public interface PipeFunction<A, B> {

    /**
     * A function that takes an argument of type A and returns a result of type B.
     *
     * @param argument An argument of type A
     * @return the result of computing the function on the argument
     */
    public B compute(A argument);
}
