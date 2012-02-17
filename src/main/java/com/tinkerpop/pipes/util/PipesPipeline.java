package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.FunctionPipe;
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
import com.tinkerpop.pipes.transform.GatherPipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.transform.MemoizePipe;
import com.tinkerpop.pipes.transform.PathFunctionPipe;
import com.tinkerpop.pipes.transform.PathPipe;
import com.tinkerpop.pipes.transform.ScatterPipe;
import com.tinkerpop.pipes.transform.SelectPipe;
import com.tinkerpop.pipes.transform.SideEffectCapPipe;
import com.tinkerpop.pipes.transform.TransformFunctionPipe;
import com.tinkerpop.pipes.util.structures.Pair;
import com.tinkerpop.pipes.util.structures.Row;
import com.tinkerpop.pipes.util.structures.Table;

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
public class PipesPipeline<S, E> extends Pipeline<S, E> implements PipesFluentPipeline<S, E> {

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

    public PipesPipeline<S, ?> exhaustMerge(final Pipe... pipes) {
        return this.add(new ExhaustMergePipe(pipes));
    }

    public PipesPipeline<S, ?> exhaustMerge() {
        return this.add(new ExhaustMergePipe((((MetaPipe) FluentUtility.removePreviousPipes(this, 1).get(0)).getPipes())));
    }

    public PipesPipeline<S, ?> fairMerge(final Pipe... pipes) {
        return this.add(new FairMergePipe(pipes));
    }

    public PipesPipeline<S, ?> fairMerge() {
        return this.add(new FairMergePipe((((MetaPipe) FluentUtility.removePreviousPipes(this, 1).get(0)).getPipes())));
    }

    public PipesPipeline<S, ?> ifThenElse(final PipeFunction<E, Boolean> ifFunction, final PipeFunction<E, ?> thenFunction, final PipeFunction<E, ?> elseFunction) {
        return this.add(new IfThenElsePipe(ifFunction, thenFunction, elseFunction));
    }

    public PipesPipeline<S, E> loop(final int numberedStep, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> whileFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), whileFunction));
    }

    public PipesPipeline<S, E> loop(final String namedStep, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> whileFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), whileFunction));
    }

    public PipesPipeline<S, E> loop(final int numberedStep, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> whileFunction, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> emitFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), whileFunction, emitFunction));
    }

    public PipesPipeline<S, E> loop(final String namedStep, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> whileFunction, final PipeFunction<LoopPipe.LoopBundle<E>, Boolean> emitFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), whileFunction, emitFunction));
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
        return this.add(new DuplicateFilterPipe<E>(dedupFunction));
    }

    public PipesPipeline<S, E> except(final Collection<E> collection) {
        return this.add(new ExceptFilterPipe<E>(collection));
    }

    public PipesPipeline<S, E> filter(final PipeFunction<E, Boolean> filterFunction) {
        return this.add(new FilterFunctionPipe<E>(filterFunction));
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
        return this.add(new AggregatePipe<E>(aggregate, aggregateFunction));
    }

    public PipesPipeline<S, E> aggregate(final PipeFunction<E, ?> aggregateFunction) {
        return this.aggregate(new ArrayList(), aggregateFunction);
    }

    public PipesPipeline<S, ?> optional(final int numberedStep) {
        return this.add(new OptionalPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    public PipesPipeline<S, ?> optional(final String namedStep) {
        return this.add(new OptionalPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    public PipesPipeline<S, E> groupBy(final Map<?, List<?>> map, final PipeFunction keyFunction, final PipeFunction valueFunction) {
        return this.add(new GroupByPipe(map, keyFunction, valueFunction));
    }

    public PipesPipeline<S, E> groupBy(final PipeFunction keyFunction, final PipeFunction valueFunction) {
        return this.add(new GroupByPipe(keyFunction, valueFunction));
    }

    public PipesPipeline<S, E> groupBy(final Map reduceMap, final PipeFunction keyFunction, final PipeFunction valueFunction, final PipeFunction reduceFunction) {
        return this.add(new GroupByReducePipe(reduceMap, keyFunction, valueFunction, reduceFunction));
    }

    public PipesPipeline<S, E> groupBy(final PipeFunction keyFunction, final PipeFunction valueFunction, final PipeFunction reduceFunction) {
        return this.add(new GroupByReducePipe(keyFunction, valueFunction, reduceFunction));
    }

    public PipesPipeline<S, E> groupCount(final Map<?, Number> map, final PipeFunction keyFunction, final PipeFunction<Pair<?, Number>, Number> valueFunction) {
        return this.add(new GroupCountFunctionPipe(map, keyFunction, valueFunction));
    }

    public PipesPipeline<S, E> groupCount(final PipeFunction keyFunction, final PipeFunction<Pair<?, Number>, Number> valueFunction) {
        return this.add(new GroupCountFunctionPipe(keyFunction, valueFunction));
    }

    public PipesPipeline<S, E> groupCount(final Map<?, Number> map, final PipeFunction keyFunction) {
        return this.add(new GroupCountFunctionPipe(map, keyFunction, null));
    }

    public PipesPipeline<S, E> groupCount(final PipeFunction keyFunction) {
        return this.add(new GroupCountFunctionPipe(keyFunction, null));
    }

    public PipesPipeline<S, E> groupCount(final Map<?, Number> map) {
        return this.add(new GroupCountPipe(map));
    }

    public PipesPipeline<S, E> groupCount() {
        return this.add(new GroupCountPipe());
    }

    public PipesPipeline<S, E> sideEffect(final PipeFunction<E, ?> sideEffectFunction) {
        return this.add(new SideEffectFunctionPipe(sideEffectFunction));
    }

    public PipesPipeline<S, E> store(final Collection<E> storage) {
        return this.add(new StorePipe<E>(storage));
    }

    public PipesPipeline<S, E> store(final Collection storage, final PipeFunction<E, ?> storageFunction) {
        return this.add(new StorePipe<E>(storage, storageFunction));
    }

    public PipesFluentPipeline<S, E> store(final PipeFunction<E, ?> storageFunction) {
        return this.store(new ArrayList(), storageFunction);
    }

    public PipesPipeline<S, E> store() {
        return this.store(new ArrayList<E>());
    }

    public PipesPipeline<S, E> table(final Table table, final Collection<String> stepNames, final PipeFunction... columnFunctions) {
        return this.add(new TablePipe(table, stepNames, FluentUtility.getAsPipes(this), columnFunctions));
    }

    public PipesPipeline<S, E> table(final Table table, final PipeFunction... columnFunctions) {
        return this.add(new TablePipe(table, null, FluentUtility.getAsPipes(this), columnFunctions));
    }

    public PipesPipeline<S, E> table(final PipeFunction... columnFunctions) {
        return this.add(new TablePipe(new Table(), null, FluentUtility.getAsPipes(this), columnFunctions));
    }

    public PipesPipeline<S, E> table(final Table table) {
        return this.add(new TablePipe(table, null, FluentUtility.getAsPipes(this)));
    }

    public PipesPipeline<S, E> table() {
        return this.add(new TablePipe(new Table(), null, FluentUtility.getAsPipes(this)));
    }

    ///////////////////////
    /// TRANSFORM PIPES ///
    ///////////////////////


    public PipesPipeline<S, List> gather() {
        return this.add(new GatherPipe());
    }

    public PipesPipeline<S, ?> gather(PipeFunction<List, ?> function) {
        this.addPipe(new GatherPipe());
        return this.add(new TransformFunctionPipe(function));
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

    public PipesPipeline<S, List> path(final PipeFunction... pathFunctions) {
        if (pathFunctions.length == 0)
            this.addPipe(new PathPipe());
        else
            this.addPipe(new PathFunctionPipe(pathFunctions));
        return (PipesPipeline<S, List>) this;
    }

    public PipesPipeline<S, Row> select(final Collection<String> stepNames, final PipeFunction... columnFunctions) {
        return this.add(new SelectPipe(stepNames, FluentUtility.getAsPipes(this), columnFunctions));
    }

    public PipesPipeline<S, Row> select(final PipeFunction... columnFunctions) {
        return this.add(new SelectPipe(null, FluentUtility.getAsPipes(this), columnFunctions));
    }

    public PipesPipeline<S, Row> select() {
        return this.add(new SelectPipe(null, FluentUtility.getAsPipes(this)));
    }

    public PipesPipeline<S, ?> scatter() {
        return this.add(new ScatterPipe());
    }

    public PipesPipeline<S, ?> cap() {
        return this.add(new SideEffectCapPipe((SideEffectPipe) FluentUtility.removePreviousPipes(this, 1).get(0)));
    }

    public <T> PipesPipeline<S, T> transform(final PipeFunction<E, T> function) {
        return this.add(new TransformFunctionPipe(function));
    }

    //////////////////////
    /// UTILITY PIPES ///
    //////////////////////

    public PipesPipeline<S, E> as(final String name) {
        return this.add(new AsPipe(name, FluentUtility.removePreviousPipes(this, 1).get(0)));
    }

    public PipesPipeline<S, S> start(final S object) {
        return this.add(new StartPipe<S>(object));
    }
}