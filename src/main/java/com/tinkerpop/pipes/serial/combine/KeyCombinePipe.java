package com.tinkerpop.pipes.serial.combine;

import com.tinkerpop.pipes.serial.AbstractPipe;
import com.tinkerpop.pipes.serial.sideeffect.SideEffectPipe;
import com.tinkerpop.pipes.serial.util.ProductPipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class KeyCombinePipe<A, B> extends AbstractPipe<ProductPipe.Pair, ProductPipe.Pair> implements SideEffectPipe<ProductPipe.Pair, ProductPipe.Pair, Map<A, List<B>>> {

    private final Map<A, List<B>> keyMap = new HashMap<A, List<B>>();

    protected ProductPipe.Pair processNextStart() {
        ProductPipe.Pair pair = this.starts.next();
        this.updateMap((A) pair.getA(), (B) pair.getB());
        return pair;
    }

    public Map<A, List<B>> getSideEffect() {
        return this.keyMap;
    }

    private void updateMap(final A a, final B b) {
        final List<B> temp = this.keyMap.get(a);
        if (null == temp) {
            List<B> tempList = new ArrayList<B>();
            tempList.add(b);
            this.keyMap.put(a, tempList);

        } else {
            temp.add(b);
        }
    }
}
