package com.tinkerpop.pipes.util.structures;

/**
 * A pair of objects.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class Pair<A, B> {

    private final A a;
    private final B b;

    public Pair(final A a, final B b) {

        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public boolean equals(Object object) {
        return (object.getClass().equals(Pair.class) && ((Pair) object).getA().equals(this.a) && ((Pair) object).getB().equals(this.b));
    }
}
