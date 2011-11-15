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
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.filter.ObjectFilterPipe;
import com.tinkerpop.pipes.filter.OrFilterPipe;
import com.tinkerpop.pipes.filter.RandomFilterPipe;
import com.tinkerpop.pipes.filter.RangeFilterPipe;
import com.tinkerpop.pipes.filter.RetainFilterPipe;
import com.tinkerpop.pipes.sideeffect.AggregatePipe;
import com.tinkerpop.pipes.sideeffect.GroupCountFunctionPipe;
import com.tinkerpop.pipes.sideeffect.GroupCountPipe;
import com.tinkerpop.pipes.sideeffect.OptionalPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectFunctionPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectPipe;
import com.tinkerpop.pipes.sideeffect.TablePipe;
import com.tinkerpop.pipes.transform.GatherPipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.transform.MemoizePipe;
import com.tinkerpop.pipes.transform.PathFunctionPipe;
import com.tinkerpop.pipes.transform.PathPipe;
import com.tinkerpop.pipes.transform.ScatterPipe;
import com.tinkerpop.pipes.transform.SideEffectCapPipe;
import com.tinkerpop.pipes.transform.TransformFunctionPipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * PipesPipeline allows for the creation of a Pipeline using the fluent-pattern (http://en.wikipedia.org/wiki/Fluent_interface ).
 * Each Pipe in Pipes maintains a fluent method in PipesPipeline.
 * Numerous method overloadings are provided in order to accommodate the various ways in which one logically thinks of a pipeline structure.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PipesPipeline<S, E> extends Pipeline<S, E> implements FluentPipeline<S, E> {

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

    public <T> PipesPipeline<S, T> step(final Pipe<?, T> pipe) {
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

    public PipesPipeline<S, ?> ifThenElse(final PipeFunction<?, Boolean> ifFunction, final PipeFunction thenFunction, final PipeFunction elseFunction) {
        return this.add(new IfThenElsePipe(ifFunction, thenFunction, elseFunction));
    }

    public PipesPipeline<S, S> loop(final int numberedStep, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> whileFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), whileFunction));
    }

    public PipesPipeline<S, S> loop(final String namedStep, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> whileFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), whileFunction));
    }

    public PipesPipeline<S, S> loop(final Pipe pipe, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> whileFunction) {
        return this.add(new LoopPipe(pipe, whileFunction));
    }

    public PipesPipeline<S, S> loop(final int numberedStep, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> whileFunction, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> emitFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), whileFunction, emitFunction));
    }

    public PipesPipeline<S, S> loop(final String namedStep, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> whileFunction, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> emitFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), whileFunction, emitFunction));
    }

    public PipesPipeline<S, S> loop(final Pipe pipe, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> whileFunction, final PipeFunction<LoopPipe.LoopBundle<S>, Boolean> emitFunction) {
        return this.add(new LoopPipe(pipe, whileFunction, emitFunction));
    }

    ////////////////////
    /// FILTER PIPES ///
    ////////////////////

    public PipesPipeline<S, E> and(final Pipe<?, Boolean>... pipes) {
        return this.add(new AndFilterPipe(pipes));
    }

    public PipesPipeline<S, ?> back(final int numberedStep) {
        return this.add(new BackFilterPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    public PipesPipeline<S, ?> back(final String namedStep) {
        return this.add(new BackFilterPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    public PipesPipeline<S, ?> back(final Pipe pipe) {
        return this.add(new BackFilterPipe(pipe));
    }

    public PipesPipeline<S, E> dedup() {
        return this.add(new DuplicateFilterPipe());
    }

    public PipesPipeline<S, E> except(final Collection collection) {
        return this.add(new ExceptFilterPipe(collection));
    }

    public PipesPipeline<S, E> filter(final PipeFunction<?, Boolean> filterFunction) {
        return this.add(new FilterFunctionPipe(filterFunction));
    }

    public PipesPipeline<S, E> objectFilter(final Object object, final FilterPipe.Filter filter) {
        return this.add(new ObjectFilterPipe(object, filter));
    }

    public PipesPipeline<S, E> or(final Pipe<S, Boolean>... pipes) {
        return this.add(new OrFilterPipe(pipes));
    }

    public PipesPipeline<S, E> random(final Double bias) {
        return this.add(new RandomFilterPipe(bias));
    }

    public PipesPipeline<S, E> range(final int low, final int high) {
        return this.add(new RangeFilterPipe(low, high));
    }

    public PipesPipeline<S, E> retain(final Collection collection) {
        return this.add(new RetainFilterPipe(collection));
    }

    public PipesPipeline<S, E> simplePath() {
        return this.add(new CyclicPathFilterPipe());
    }

    /////////////////////////
    /// SIDE-EFFECT PIPES ///
    /////////////////////////

    public PipesPipeline<S, E> aggregate(final Collection aggregate) {
        return this.add(new AggregatePipe(aggregate));
    }

    public PipesPipeline<S, E> aggregate(final Collection aggregate, final PipeFunction aggregateFunction) {
        return this.add(new AggregatePipe(aggregate, aggregateFunction));
    }

    public PipesPipeline<S, E> aggregate() {
        return this.aggregate(new ArrayList());
    }

    public PipesPipeline<S, E> aggregate(final PipeFunction aggregateFunction) {
        return this.aggregate(new ArrayList(), aggregateFunction);
    }

    public PipesPipeline<S, ?> optional(final int numberedStep) {
        return this.add(new OptionalPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    public PipesPipeline<S, ?> optional(final String namedStep) {
        return this.add(new OptionalPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    public PipesPipeline<S, ?> optional(final Pipe pipe) {
        return this.add(new OptionalPipe(pipe));
    }

    public PipesPipeline<S, E> groupCount(final Map<?, Number> map, final PipeFunction keyFunction, final PipeFunction<Number, Number> valueFunction) {
        return this.add(new GroupCountFunctionPipe(map, keyFunction, valueFunction));
    }

    public PipesPipeline<S, E> groupCount(final PipeFunction keyFunction, final PipeFunction<Number, Number> valueFunction) {
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

    public PipesPipeline<S, E> sideEffect(final PipeFunction sideEffectFunction) {
        return this.add(new SideEffectFunctionPipe(sideEffectFunction));
    }

    public PipesPipeline<S, E> table(final Table table, final Collection<String> stepNames, final PipeFunction... columnFunctions) {
        return this.add(new TablePipe(table, stepNames, FluentUtility.getAsPipes(this), columnFunctions));
    }

    public PipesPipeline<S, E> table(final Table table, final PipeFunction... columnFunctions) {
        return this.add(new TablePipe(table, null, FluentUtility.getAsPipes(this), columnFunctions));
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


    public PipesPipeline<S, List<?>> gather() {
        return this.add(new GatherPipe());
    }

    public PipesPipeline<S, ?> gather(PipeFunction<List, ?> function) {
        this.addPipe(new GatherPipe());
        return this.add(new TransformFunctionPipe(function));
    }

    public PipesPipeline<S, E> _() {
        return this.add(new IdentityPipe());
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

    public PipesPipeline<S, List<?>> path(final PipeFunction... pathFunctions) {
        if (pathFunctions.length == 0)
            this.addPipe(new PathPipe());
        else
            this.addPipe(new PathFunctionPipe(pathFunctions));
        return (PipesPipeline<S, List<?>>) this;
    }

    public PipesPipeline<S, ?> scatter() {
        return this.add(new ScatterPipe());
    }

    public PipesPipeline<S, ?> cap() {
        return this.add(new SideEffectCapPipe((SideEffectPipe) FluentUtility.removePreviousPipes(this, 1).get(0)));
    }

    public PipesPipeline<S, ?> transform(final PipeFunction function) {
        return this.add(new TransformFunctionPipe(function));
    }

    //////////////////////
    /// UTILITY PIPES ///
    //////////////////////

    public PipesPipeline<S, E> as(final String name) {
        return this.add(new AsPipe(name, FluentUtility.removePreviousPipes(this, 1).get(0)));
    }

    public PipesPipeline<S, E> start(final Object object) {
        return this.add(new StartPipe(object));
    }

    ///////////////////////
    /// UTILITY METHODS ///
    ///////////////////////

    public long count() {
        return PipeHelper.counter(this);
    }

    public void iterate() {
        PipeHelper.iterate(this);
    }

    public List<E> next(final int number) {
        final List<E> list = new ArrayList<E>(number);
        PipeHelper.fillCollection(this, list, number);
        return list;
    }

    public List<E> toList() {
        final List<E> list = new ArrayList<E>();
        PipeHelper.fillCollection(this, list);
        return list;
    }

    public Collection<E> fill(final Collection<E> collection) {
        PipeHelper.fillCollection(this, collection);
        return collection;
    }

}