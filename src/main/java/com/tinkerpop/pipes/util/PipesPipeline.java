package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.FunctionPipe;
import com.tinkerpop.pipes.IdentityPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.CopySplitPipe;
import com.tinkerpop.pipes.branch.ExhaustMergePipe;
import com.tinkerpop.pipes.branch.FairMergePipe;
import com.tinkerpop.pipes.branch.IfThenElsePipe;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.filter.AndFilterPipe;
import com.tinkerpop.pipes.filter.BackFilterPipe;
import com.tinkerpop.pipes.filter.CyclicPathFilterPipe;
import com.tinkerpop.pipes.filter.DuplicateFilterPipe;
import com.tinkerpop.pipes.filter.ExceptFilterPipe;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.filter.OrFilterPipe;
import com.tinkerpop.pipes.filter.RandomFilterPipe;
import com.tinkerpop.pipes.filter.RangeFilterPipe;
import com.tinkerpop.pipes.filter.RetainFilterPipe;
import com.tinkerpop.pipes.sideeffect.AggregatePipe;
import com.tinkerpop.pipes.sideeffect.GroupByPipe;
import com.tinkerpop.pipes.sideeffect.GroupByReducePipe;
import com.tinkerpop.pipes.sideeffect.GroupCountFunctionPipe;
import com.tinkerpop.pipes.sideeffect.GroupCountPipe;
import com.tinkerpop.pipes.sideeffect.OptionalPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectFunctionPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectPipe;
import com.tinkerpop.pipes.sideeffect.StorePipe;
import com.tinkerpop.pipes.sideeffect.TablePipe;
import com.tinkerpop.pipes.sideeffect.TreePipe;
import com.tinkerpop.pipes.transform.GatherFunctionPipe;
import com.tinkerpop.pipes.transform.GatherPipe;
import com.tinkerpop.pipes.transform.MemoizePipe;
import com.tinkerpop.pipes.transform.OrderMapPipe;
import com.tinkerpop.pipes.transform.OrderPipe;
import com.tinkerpop.pipes.transform.PathPipe;
import com.tinkerpop.pipes.transform.ScatterPipe;
import com.tinkerpop.pipes.transform.SelectPipe;
import com.tinkerpop.pipes.transform.ShufflePipe;
import com.tinkerpop.pipes.transform.SideEffectCapPipe;
import com.tinkerpop.pipes.transform.TransformFunctionPipe;
import com.tinkerpop.pipes.transform.TransformPipe;
import com.tinkerpop.pipes.util.structures.AsMap;
import com.tinkerpop.pipes.util.structures.Pair;
import com.tinkerpop.pipes.util.structures.Row;
import com.tinkerpop.pipes.util.structures.Table;
import com.tinkerpop.pipes.util.structures.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * PipesPipeline allows for the creation of a Pipeline using the fluent-pattern (http://en.wikipedia.org/wiki/Fluent_interface).
 * Each Pipe in Pipes maintains a fluent method in PipesPipeline.
 * Numerous method overloadings are provided in order to accommodate the various ways in which one logically thinks of a pipeline structure.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
@Deprecated
public class PipesPipeline<S, E> extends Pipeline<S, E> implements PipesFluentPipeline<S, E> {

    private final AsMap asMap = new AsMap(this);

    public PipesPipeline() {
        super();
    }

    public PipesPipeline(final Object starts) {
        super(new StartPipe(starts));
        FluentUtility.setStarts(this, starts);
    }

    public <T> PipesPipeline<S, T> add(final Pipe<?, T> pipe) {
        this.addPipe(pipe);
        return (PipesPipeline<S, T>) this;
    }

    public PipesPipeline<S, ?> step(final PipeFunction function) {
        return this.add(new FunctionPipe(function));
    }

    public <T> PipesPipeline<S, T> step(final Pipe<E, T> pipe) {
        return this.add(pipe);
    }

    ////////////////////
    /// BRANCH PIPES ///
    ////////////////////

    public PipesPipeline<S, ?> copySplit(final Pipe... pipes) {
        return this.add(new CopySplitPipe(pipes));
    }

    public PipesPipeline<S, ?> exhaustMerge() {
        return this.add(new ExhaustMergePipe((((MetaPipe) FluentUtility.getPreviousPipe(this)).getPipes())));
    }

    public PipesPipeline<S, ?> fairMerge() {
        return this.add(new FairMergePipe((((MetaPipe) FluentUtility.getPreviousPipe(this)).getPipes())));
    }

    public PipesPipeline<S, ?> ifThenElse(final PipeFunction<E, Boolean> ifFunction, final PipeFunction<E, ?> thenFunction, final PipeFunction<E, ?> elseFunction) {
        return this.add(new IfThenElsePipe(FluentUtility.prepareFunction(this.asMap, ifFunction), FluentUtility.prepareFunction(this.asMap, thenFunction), FluentUtility.prepareFunction(this.asMap, elseFunction)));
    }

    public PipesPipeline<S, E> loop(final int numberedStep, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> whileFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), FluentUtility.prepareFunction(this.asMap, whileFunction)));
    }

    public PipesPipeline<S, E> loop(final String namedStep, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> whileFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), FluentUtility.prepareFunction(this.asMap, whileFunction)));
    }

    public PipesPipeline<S, E> loop(final int numberedStep, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> whileFunction, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> emitFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), FluentUtility.prepareFunction(this.asMap, whileFunction), FluentUtility.prepareFunction(this.asMap, emitFunction)));
    }

    public PipesPipeline<S, E> loop(final String namedStep, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> whileFunction, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> emitFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), FluentUtility.prepareFunction(this.asMap, whileFunction), FluentUtility.prepareFunction(this.asMap, emitFunction)));
    }

    ////////////////////
    /// FILTER PIPES ///
    ////////////////////

    public PipesPipeline<S, E> and(final Pipe<E, ?>... pipes) {
        return this.add(new AndFilterPipe<E>(pipes));
    }

    public PipesPipeline<S, ?> back(final int numberedStep) {
        return this.add(new BackFilterPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    public PipesPipeline<S, ?> back(final String namedStep) {
        return this.add(new BackFilterPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    public PipesPipeline<S, E> dedup() {
        return this.add(new DuplicateFilterPipe<E>());
    }

    public PipesPipeline<S, E> dedup(final PipeFunction<E, ?> dedupFunction) {
        return this.add(new DuplicateFilterPipe<E>(FluentUtility.prepareFunction(this.asMap, dedupFunction)));
    }

    public PipesPipeline<S, E> except(final Collection<E> collection) {
        return this.add(new ExceptFilterPipe<E>(collection));
    }

    public PipesPipeline<S, E> except(final String... namedSteps) {
        return this.add(new ExceptFilterPipe<E>(this.asMap, namedSteps));
    }

    public PipesPipeline<S, E> filter(final PipeFunction<E, Boolean> filterFunction) {
        return this.add(new FilterFunctionPipe<E>(FluentUtility.prepareFunction(this.asMap, filterFunction)));
    }

    public PipesPipeline<S, E> or(final Pipe<E, ?>... pipes) {
        return this.add(new OrFilterPipe<E>(pipes));
    }

    public PipesPipeline<S, E> random(final Double bias) {
        return this.add(new RandomFilterPipe<E>(bias));
    }

    public PipesPipeline<S, E> range(final int low, final int high) {
        return this.add(new RangeFilterPipe<E>(low, high));
    }

    public PipesPipeline<S, E> retain(final Collection<E> collection) {
        return this.add(new RetainFilterPipe<E>(collection));
    }

    public PipesPipeline<S, E> retain(final String... namedSteps) {
        return this.add(new RetainFilterPipe<E>(this.asMap, namedSteps));
    }

    public PipesPipeline<S, E> simplePath() {
        return this.add(new CyclicPathFilterPipe<E>());
    }

    /////////////////////////
    /// SIDE-EFFECT PIPES ///
    /////////////////////////

    public PipesPipeline<S, E> aggregate() {
        return this.aggregate(new ArrayList<E>());
    }

    public PipesPipeline<S, E> aggregate(final Collection<E> aggregate) {
        return this.add(new AggregatePipe<E>(aggregate));
    }

    public PipesPipeline<S, E> aggregate(final Collection aggregate, final PipeFunction<E, ?> aggregateFunction) {
        return this.add(new AggregatePipe<E>(aggregate, FluentUtility.prepareFunction(this.asMap, aggregateFunction)));
    }

    public PipesPipeline<S, E> aggregate(final PipeFunction<E, ?> aggregateFunction) {
        return this.aggregate(new ArrayList(), FluentUtility.prepareFunction(this.asMap, aggregateFunction));
    }

    public PipesPipeline<S, ?> optional(final int numberedStep) {
        return this.add(new OptionalPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    public PipesPipeline<S, ?> optional(final String namedStep) {
        return this.add(new OptionalPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    public PipesPipeline<S, E> groupBy(final Map<?, List<?>> map, final PipeFunction keyFunction, final PipeFunction valueFunction) {
        return this.add(new GroupByPipe(map, FluentUtility.prepareFunction(this.asMap, keyFunction), FluentUtility.prepareFunction(this.asMap, valueFunction)));
    }

    public PipesPipeline<S, E> groupBy(final PipeFunction keyFunction, final PipeFunction valueFunction) {
        return this.add(new GroupByPipe(FluentUtility.prepareFunction(this.asMap, keyFunction), FluentUtility.prepareFunction(this.asMap, valueFunction)));
    }

    public PipesPipeline<S, E> groupBy(final Map reduceMap, final PipeFunction keyFunction, final PipeFunction valueFunction, final PipeFunction reduceFunction) {
        return this.add(new GroupByReducePipe(reduceMap, FluentUtility.prepareFunction(this.asMap, keyFunction), FluentUtility.prepareFunction(this.asMap, valueFunction), FluentUtility.prepareFunction(this.asMap, reduceFunction)));
    }

    public PipesPipeline<S, E> groupBy(final PipeFunction keyFunction, final PipeFunction valueFunction, final PipeFunction reduceFunction) {
        return this.add(new GroupByReducePipe(FluentUtility.prepareFunction(this.asMap, keyFunction), FluentUtility.prepareFunction(this.asMap, valueFunction), FluentUtility.prepareFunction(this.asMap, reduceFunction)));
    }

    public PipesPipeline<S, E> groupCount(final Map<?, Number> map, final PipeFunction keyFunction, final PipeFunction<Pair<?, Number>, Number> valueFunction) {
        return this.add(new GroupCountFunctionPipe(map, FluentUtility.prepareFunction(this.asMap, keyFunction), FluentUtility.prepareFunction(this.asMap, valueFunction)));
    }

    public PipesPipeline<S, E> groupCount(final PipeFunction keyFunction, final PipeFunction<Pair<?, Number>, Number> valueFunction) {
        return this.add(new GroupCountFunctionPipe(FluentUtility.prepareFunction(this.asMap, keyFunction), FluentUtility.prepareFunction(this.asMap, valueFunction)));
    }

    public PipesPipeline<S, E> groupCount(final Map<?, Number> map, final PipeFunction keyFunction) {
        return this.add(new GroupCountFunctionPipe(map, FluentUtility.prepareFunction(this.asMap, keyFunction), null));
    }

    public PipesPipeline<S, E> groupCount(final PipeFunction keyFunction) {
        return this.add(new GroupCountFunctionPipe(FluentUtility.prepareFunction(this.asMap, keyFunction), null));
    }

    public PipesPipeline<S, E> groupCount(final Map<?, Number> map) {
        return this.add(new GroupCountPipe(map));
    }

    public PipesPipeline<S, E> groupCount() {
        return this.add(new GroupCountPipe<E>());
    }

    public PipesPipeline<S, E> sideEffect(final PipeFunction<E, ?> sideEffectFunction) {
        return this.add(new SideEffectFunctionPipe<E>(FluentUtility.prepareFunction(this.asMap, sideEffectFunction)));
    }

    public PipesPipeline<S, E> store(final Collection<E> storage) {
        return this.add(new StorePipe<E>(storage));
    }

    public PipesPipeline<S, E> store(final Collection storage, final PipeFunction<E, ?> storageFunction) {
        return this.add(new StorePipe<E>(storage, FluentUtility.prepareFunction(this.asMap, storageFunction)));
    }

    public PipesFluentPipeline<S, E> store(final PipeFunction<E, ?> storageFunction) {
        return this.store(new ArrayList(), FluentUtility.prepareFunction(this.asMap, storageFunction));
    }

    public PipesPipeline<S, E> store() {
        return this.store(new ArrayList<E>());
    }

    public PipesPipeline<S, E> table(final Table table, final Collection<String> stepNames, final PipeFunction... columnFunctions) {
        return this.add(new TablePipe<E>(table, stepNames, FluentUtility.getAsPipes(this), FluentUtility.prepareFunctions(this.asMap, columnFunctions)));
    }

    public PipesPipeline<S, E> table(final Table table, final PipeFunction... columnFunctions) {
        return this.add(new TablePipe<E>(table, null, FluentUtility.getAsPipes(this), FluentUtility.prepareFunctions(this.asMap, columnFunctions)));
    }

    public PipesPipeline<S, E> table(final PipeFunction... columnFunctions) {
        return this.add(new TablePipe<E>(new Table(), null, FluentUtility.getAsPipes(this), FluentUtility.prepareFunctions(this.asMap, columnFunctions)));
    }

    public PipesPipeline<S, E> table(final Table table) {
        return this.add(new TablePipe<E>(table, null, FluentUtility.getAsPipes(this)));
    }

    public PipesPipeline<S, E> table() {
        return this.add(new TablePipe<E>(new Table(), null, FluentUtility.getAsPipes(this)));
    }

    public PipesPipeline<S, E> tree(final Tree tree, final PipeFunction... branchFunctions) {
        return this.add(new TreePipe<E>(tree, FluentUtility.prepareFunctions(this.asMap, branchFunctions)));
    }

    public PipesPipeline<S, E> tree(final PipeFunction... branchFunctions) {
        return this.add(new TreePipe<E>(FluentUtility.prepareFunctions(this.asMap, branchFunctions)));
    }

    ///////////////////////
    /// TRANSFORM PIPES ///
    ///////////////////////


    public PipesPipeline<S, List> gather() {
        return this.add(new GatherPipe());
    }

    public PipesPipeline<S, ?> gather(PipeFunction<List, ?> function) {
        return this.add(new GatherFunctionPipe(FluentUtility.prepareFunction(this.asMap, function)));
    }

    public PipesPipeline<S, E> _() {
        return this.add(new IdentityPipe<E>());
    }

    public PipesPipeline<S, E> memoize(final String namedStep) {
        return this.add(new MemoizePipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    public PipesPipeline<S, E> memoize(final int numberedStep) {
        return this.add(new MemoizePipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    public PipesPipeline<S, E> memoize(final String namedStep, final Map map) {
        return this.add(new MemoizePipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), map));
    }

    public PipesPipeline<S, E> memoize(final int numberedStep, final Map map) {
        return this.add(new MemoizePipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), map));
    }

    public PipesPipeline<S, E> order() {
        return this.add(new OrderPipe());
    }

    public PipesPipeline<S, E> order(TransformPipe.Order order) {
        return this.add(new OrderPipe(order));
    }

    public PipesPipeline<S, E> order(final PipeFunction<Pair<E, E>, Integer> compareFunction) {
        return this.add(new OrderPipe(FluentUtility.prepareFunction(this.asMap, compareFunction)));
    }

    public PipesPipeline<S, List> path(final PipeFunction... pathFunctions) {
        return this.add(new PathPipe<Object>(FluentUtility.prepareFunctions(this.asMap, pathFunctions)));
    }

    public PipesPipeline<S, Row> select(final Collection<String> stepNames, final PipeFunction... columnFunctions) {
        return this.add(new SelectPipe(stepNames, FluentUtility.getAsPipes(this), FluentUtility.prepareFunctions(this.asMap, columnFunctions)));
    }

    public PipesPipeline<S, Row> select(final PipeFunction... columnFunctions) {
        return this.add(new SelectPipe(null, FluentUtility.getAsPipes(this), FluentUtility.prepareFunctions(this.asMap, columnFunctions)));
    }

    public PipesPipeline<S, Row> select() {
        return this.add(new SelectPipe(null, FluentUtility.getAsPipes(this)));
    }

    public PipesPipeline<S, ?> scatter() {
        return this.add(new ScatterPipe());
    }

    public PipesPipeline<S, List> shuffle() {
        return this.add(new ShufflePipe());
    }

    public PipesPipeline<S, ?> cap() {
        return this.add(new SideEffectCapPipe((SideEffectPipe) FluentUtility.removePreviousPipes(this, 1).get(0)));
    }

    public PipesPipeline<S, ?> orderMap(final TransformPipe.Order order) {
        return this.add(new OrderMapPipe<Object>(order));
    }

    public PipesPipeline<S, ?> orderMap(final PipeFunction<Pair<Map.Entry, Map.Entry>, Integer> compareFunction) {
        return this.add(new OrderMapPipe(FluentUtility.prepareFunction(this.asMap, compareFunction)));
    }

    public <T> PipesPipeline<S, T> transform(final PipeFunction<E, T> function) {
        return this.add(new TransformFunctionPipe(FluentUtility.prepareFunction(this.asMap, function)));
    }

    //////////////////////
    /// UTILITY PIPES ///
    //////////////////////

    public PipesPipeline<S, E> as(final String name) {
        final PipesPipeline<S, E> pipeline = this.add(new AsPipe(name, FluentUtility.removePreviousPipes(this, 1).get(0)));
        this.asMap.refresh();
        return pipeline;
    }

    public PipesPipeline<S, S> start(final S object) {
        return this.add(new StartPipe<S>(object));
    }

    public PipesFluentPipeline<S, E> enablePath() {
        this.enablePath(true);
        return this;
    }

    /**
     * Returns the current pipeline with a new end type.
     * Useful if the end type of the pipeline cannot be implicitly derived.
     *
     * @return returns the current pipeline with the new end type.
     */
    public <E> PipesPipeline<S, E> cast(Class<E> end) {
        return (PipesPipeline<S, E>) this;
    }
}