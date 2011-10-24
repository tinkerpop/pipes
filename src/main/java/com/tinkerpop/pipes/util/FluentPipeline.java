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
import com.tinkerpop.pipes.transform.HasNextPipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.transform.MemoizePipe;
import com.tinkerpop.pipes.transform.PathFunctionPipe;
import com.tinkerpop.pipes.transform.PathPipe;
import com.tinkerpop.pipes.transform.ScatterPipe;
import com.tinkerpop.pipes.transform.SideEffectCapPipe;
import com.tinkerpop.pipes.transform.TransformFunctionPipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * FluentPipeline allows for the creation of a Pipeline using the fluent-pattern (http://en.wikipedia.org/wiki/Fluent_interface ).
 * Each Pipe in Pipes maintains a fluent method in FluentPipeline.
 * Numerous method overloadings are provided in order to accommodate the various ways in which one logically thinks of a pipeline structure.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FluentPipeline<S, E, T extends FluentPipeline> extends Pipeline<S, E> {

    public FluentPipeline() {
        super();
    }

    /**
     * Construct a new FluentPipeline with a StartPipe as the first pipe given provide start object.
     *
     * @param starts start object (if iterable/iterator, it is unfolded)
     */
    public FluentPipeline(final Object starts) {
        super(new StartPipe(starts));
        if (starts instanceof Iterator) {
            this.starts = (Iterator) starts;
        } else if (starts instanceof Iterable) {
            this.starts = ((Iterable) starts).iterator();
        } else {
            this.starts = new SingleIterator(starts);
        }
    }

    /**
     * Add an arbitrary Pipe to the end of the pipeline.
     *
     * @param pipe the pipe to concatenate to the end of the pipeline
     * @return the extended FluentPipeline
     */
    public T add(final Pipe pipe) {
        this.addPipe(pipe);
        return (T) this;
    }

    /**
     * Add a FunctionPipe to the end of the pipeline.
     *
     * @param function the function of the FunctionPipe
     * @return the extended FluentPipeline
     */
    public T step(final PipeFunction function) {
        return this.add(new FunctionPipe(function));
    }

    public T step(final Pipe pipe) {
        return this.add(pipe);
    }

    ////////////////////
    /// BRANCH PIPES ///
    ////////////////////

    /**
     * Add a CopySplitPipe to the end of the pipeline.
     *
     * @param pipes the internal pipes of the CopySplitPipe
     * @return the extended FluentPipeline
     */
    public T copySplit(final Pipe... pipes) {
        return this.add(new CopySplitPipe(pipes));
    }

    /**
     * Add an ExhaustMergePipe to the end of the pipeline.
     *
     * @param pipes the internal pipes ExhaustMergePipe
     * @return the extended FluentPipeline
     */
    public T exhaustMerge(final Pipe... pipes) {
        return this.add(new ExhaustMergePipe(pipes));
    }

    /**
     * Add an ExhaustMergePipe to the end of the pipeline.
     * The one-step previous MetaPipe in the pipeline's pipes are used as the internal pipes.
     *
     * @return the extended FluentPipeline
     */
    public T exhaustMerge() {
        return this.add(new ExhaustMergePipe((((MetaPipe) FluentUtility.removePreviousPipes(this, 1).get(0)).getPipes())));
    }

    /**
     * Add a FairMergePipe to the end of the pipeline.
     *
     * @param pipes the internal pipes of the FairMergePipe
     * @return the extended FluentPipeline
     */
    public T fairMerge(final Pipe... pipes) {
        return this.add(new FairMergePipe(pipes));
    }

    /**
     * Add a FairMergePipe to the end of the Pipeline.
     * The one-step previous MetaPipe in the pipeline's pipes are used as the internal pipes.
     *
     * @return the extended FluentPipeline
     */
    public T fairMerge() {
        return this.add(new FairMergePipe((((MetaPipe) FluentUtility.removePreviousPipes(this, 1).get(0)).getPipes())));
    }

    /**
     * Add an IfThenElsePipe to the end of the Pipeline.
     *
     * @param ifFunction   the function denoting the "if" part of the pipe
     * @param thenFunction the function denoting the "then" part of the pipe
     * @param elseFunction the function denoting the "else" part of the pipe
     * @return the extended FluentPipeline
     */
    public T ifThenElse(final PipeFunction<?, Boolean> ifFunction, final PipeFunction thenFunction, final PipeFunction elseFunction) {
        return this.add(new IfThenElsePipe(ifFunction, thenFunction, elseFunction));
    }

    /**
     * Add a LoopPipe to the end of the Pipeline.
     *
     * @param numberedStep  the number of steps previous to loop back to
     * @param whileFunction the "while()" whileFunction of the LoopPipe
     * @return the extended FluentPipeline
     */
    public T loop(final int numberedStep, final PipeFunction<?, Boolean> whileFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), whileFunction));
    }

    /**
     * Add a LoopPipe ot the end of the Pipeline.
     *
     * @param namedStep     the name of the step previous to loop back to
     * @param whileFunction the "while()" function of the LoopPipe
     * @return the extended FluentPipeline
     */
    public T loop(final String namedStep, final PipeFunction<?, Boolean> whileFunction) {
        return this.add(new LoopPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), whileFunction));
    }

    /**
     * Add a LoopPipe ot the end of the Pipeline.
     *
     * @param pipe          the internal pipe of the LoopPipe
     * @param whileFunction the "while()" function of the LoopPipe
     * @return the extended FluentPipeline
     */
    public T loop(final Pipe pipe, final PipeFunction<?, Boolean> whileFunction) {
        return this.add(new LoopPipe(pipe, whileFunction));
    }

    ////////////////////
    /// FILTER PIPES ///
    ////////////////////

    /**
     * Add an AndFilterPipe to the end the Pipeline.
     *
     * @param pipes the internal pipes of the AndFilterPipe
     * @return the extended FluentPipeline
     */
    public T and(final Pipe<S, Boolean>... pipes) {
        return this.add(new AndFilterPipe(pipes));
    }

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to back up to
     * @return the extended FluentPipeline
     */
    public T back(final int numberedStep) {
        return this.add(new BackFilterPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to back up to
     * @return the extended FluentPipeline
     */
    public T back(final String namedStep) {
        return this.add(new BackFilterPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the BackFilterPipe
     * @return the extended FluentPipeline
     */
    public T back(final Pipe pipe) {
        return this.add(new BackFilterPipe(pipe));
    }

    /**
     * Add a DuplicateFilterPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T dedup() {
        return this.add(new DuplicateFilterPipe());
    }

    /**
     * Add an ExceptFilterPipe to the end of the Pipeline.
     *
     * @param collection the collection except from the stream
     * @return the extended FluentPipeline
     */
    public T except(final Collection collection) {
        return this.add(new ExceptFilterPipe(collection));
    }

    /**
     * Add an FilterFunctionPipe to the end of the Pipeline.
     *
     * @param filterFunction the filter function of the pipe
     * @return the extended FluentPipeline
     */
    public T filter(final PipeFunction<?, Boolean> filterFunction) {
        return this.add(new FilterFunctionPipe(filterFunction));
    }

    /**
     * Add an ObjectFilterPipe to the end of the Pipeline.
     *
     * @param object the object to filter on
     * @param filter the filter of the pipe
     * @return the extended FluentPipeline
     */
    public T objectFilter(final Object object, final FilterPipe.Filter filter) {
        return this.add(new ObjectFilterPipe(object, filter));
    }

    /**
     * Add an OrFilterPipe to the end the Pipeline.
     *
     * @param pipes the internal pipes of the OrFilterPipe
     * @return the extended FluentPipeline
     */
    public T or(final Pipe<S, Boolean>... pipes) {
        return this.add(new OrFilterPipe(pipes));
    }

    /**
     * Add a RandomFilterPipe to the end of the Pipeline.
     *
     * @param bias the bias of the random coin
     * @return the extended FluentPipeline
     */
    public T random(final Double bias) {
        return this.add(new RandomFilterPipe(bias));
    }

    /**
     * Add a RageFilterPipe to the end of the Pipeline.
     *
     * @param low  the low end of the range
     * @param high the high end of the range
     * @return the extended FluentPipeline
     */
    public T range(final int low, final int high) {
        return this.add(new RangeFilterPipe(low, high));
    }

    /**
     * Add a RetainFilterPipe to the end of the Pipeline.
     *
     * @param collection the collection to retain
     * @return the extended FluentPipeline
     */
    public T retain(final Collection collection) {
        return this.add(new RetainFilterPipe(collection));
    }

    /**
     * Add a CyclicPathFilterPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T simplePath() {
        return this.add(new CyclicPathFilterPipe());
    }

    /////////////////////////
    /// SIDE-EFFECT PIPES ///
    /////////////////////////

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     *
     * @param aggregate the collection to aggregate results into
     * @return the extended FluentPipeline
     */
    public T aggregate(final Collection aggregate) {
        return this.add(new AggregatePipe(aggregate));
    }

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     *
     * @param aggregate         the collection to aggregate results into
     * @param aggregateFunction the function to run over each object prior to insertion into the aggregate
     * @return the extended FluentPipeline
     */
    public T aggregate(final Collection aggregate, final PipeFunction aggregateFunction) {
        return this.add(new AggregatePipe(aggregate, aggregateFunction));
    }

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     * An ArrayList aggregate is provided.
     *
     * @return the extended FluentPipeline
     */
    public T aggregate() {
        return this.aggregate(new ArrayList());
    }

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     * An ArrayList aggregate is provided.
     *
     * @param aggregateFunction the function to run over each object prior to insertion into the aggregate
     * @return the extended FluentPipeline
     */
    public T aggregate(final PipeFunction aggregateFunction) {
        return this.aggregate(new ArrayList(), aggregateFunction);
    }

    // todo do count pipe? (or remove it)

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to optional back to
     * @return the extended FluentPipeline
     */
    public T optional(final int numberedStep) {
        return this.add(new OptionalPipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to optional back to
     * @return the extended FluentPipeline
     */
    public T optional(final String namedStep) {
        return this.add(new OptionalPipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the OptionalPipe
     * @return the extended FluentPipeline
     */
    public T optional(final Pipe pipe) {
        return this.add(new OptionalPipe(pipe));
    }

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param map           a provided count map
     * @param keyFunction   the key function to determine map key
     * @param valueFunction the value function to determine map value
     * @return the extended FluentPipeline
     */
    public T groupCount(final Map<?, Number> map, final PipeFunction keyFunction, final PipeFunction<Number, Number> valueFunction) {
        return this.add(new GroupCountFunctionPipe(map, keyFunction, valueFunction));
    }

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param keyFunction   the key function to determine map key
     * @param valueFunction the value function to determine map value
     * @return the extended FluentPipeline
     */
    public T groupCount(final PipeFunction keyFunction, final PipeFunction<Number, Number> valueFunction) {
        return this.add(new GroupCountFunctionPipe(keyFunction, valueFunction));
    }


    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param map         a provided count map
     * @param keyFunction the key function to determine map key
     * @return the extended FluentPipeline
     */
    public T groupCount(final Map<?, Number> map, final PipeFunction keyFunction) {
        return this.add(new GroupCountFunctionPipe(map, keyFunction, null));
    }

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param keyFunction the key function to determine map key
     * @return the extended FluentPipeline
     */
    public T groupCount(final PipeFunction keyFunction) {
        return this.add(new GroupCountFunctionPipe(keyFunction, null));
    }


    /**
     * Add a GroupCountPipe to the end of the Pipeline.
     *
     * @param map a provided count map
     * @return the extended FluentPipeline
     */
    public T groupCount(final Map<?, Number> map) {
        return this.add(new GroupCountPipe(map));
    }

    /**
     * Add a GroupCountPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T groupCount() {
        return this.add(new GroupCountPipe());
    }


    /**
     * Add a SideEffectFunctionPipe to the end of the Pipeline.
     *
     * @param sideEffectFunction the function of the pipe
     * @return the extended FluentPipeline
     */
    public T sideEffect(final PipeFunction sideEffectFunction) {
        return this.add(new SideEffectFunctionPipe(sideEffectFunction));
    }

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table           the table to fill
     * @param stepNames       the named steps to include in the filling
     * @param columnFunctions the post-processing function for each column
     * @return the extended FluentPipeline
     */
    public T table(final Table table, final Collection<String> stepNames, final PipeFunction... columnFunctions) {
        return this.add(new TablePipe(table, stepNames, FluentUtility.getAsPipes(this), columnFunctions));
    }

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table           the table to fill
     * @param columnFunctions the post-processing function for each column
     * @return the extended FluentPipeline
     */
    public T table(final Table table, final PipeFunction... columnFunctions) {
        return this.add(new TablePipe(table, null, FluentUtility.getAsPipes(this), columnFunctions));
    }

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table the table to fill
     * @return the extended FluentPipeline
     */
    public T table(final Table table) {
        return this.add(new TablePipe(table, null, FluentUtility.getAsPipes(this)));
    }

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T table() {
        return this.add(new TablePipe(new Table(), null, FluentUtility.getAsPipes(this)));
    }

    ///////////////////////
    /// TRANSFORM PIPES ///
    ///////////////////////


    /**
     * Add a GatherPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T gather() {
        return this.add(new GatherPipe());
    }

    public T gather(PipeFunction<List, ?> function) {
        this.addPipe(new GatherPipe());
        return this.add(new TransformFunctionPipe(function));
    }

    public T hasNext(final Pipe pipe) {
        return this.add(new HasNextPipe(pipe));
    }

    /**
     * Add an IdentityPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T identity() {
        return this.add(new IdentityPipe());
    }

    /**
     * Add an IdentityPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T _() {
        return this.identity();
    }

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to memoize to
     * @return the extended FluentPipeline
     */
    public T memoize(final String namedStep) {
        return this.add(new MemoizePipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep))));
    }

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param numberedStep the number of the step previous to memoize to
     * @return the extended FluentPipeline
     */
    public T memoize(final int numberedStep) {
        return this.add(new MemoizePipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep))));
    }

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to memoize to
     * @param map       the memoization map
     * @return the extended FluentPipeline
     */
    public T memoize(final String namedStep, final Map map) {
        return this.add(new MemoizePipe(new Pipeline(FluentUtility.removePreviousPipes(this, namedStep)), map));
    }

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param numberedStep the number of the step previous to memoize to
     * @param map          the memoization map
     * @return the extended FluentPipeline
     */
    public T memoize(final int numberedStep, final Map map) {
        return this.add(new MemoizePipe(new Pipeline(FluentUtility.removePreviousPipes(this, numberedStep)), map));
    }

    /**
     * Add a PathPipe or PathFunctionPipe to the end of the Pipeline.
     *
     * @param pathFunctions the path function of the PathFunctionPipe
     * @return the extended FluentPipeline
     */
    public T path(final PipeFunction... pathFunctions) {
        if (pathFunctions.length == 0)
            this.addPipe(new PathPipe());
        else
            this.addPipe(new PathFunctionPipe(pathFunctions));
        return (T) this;
    }

    /**
     * Add a PathPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T path() {
        return this.add(new PathPipe());
    }

    /**
     * Add a ScatterPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T scatter() {
        return this.add(new ScatterPipe());
    }

    /**
     * Add a SideEffectCapPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T sideEffectCap() {
        return this.add(new SideEffectCapPipe((SideEffectPipe) FluentUtility.removePreviousPipes(this, 1).get(0)));
    }

    /**
     * Add a SideEffectCapPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public T cap() {
        return this.sideEffectCap();
    }

    /**
     * Add a TransformFunctionPipe to the end of the Pipeline.
     *
     * @param function the transformation function of the pipe
     * @return the extended FluentPipeline
     */
    public T transform(final PipeFunction function) {
        return this.add(new TransformFunctionPipe(function));
    }

    //////////////////////
    /// UTILITY PIPES ///
    //////////////////////

    /**
     * Wrap the previous step in an AsPipe
     *
     * @param name the name of the AsPipe
     * @return the extended FluentPipeline
     */
    public T as(final String name) {
        return this.add(new AsPipe(name, FluentUtility.removePreviousPipes(this, 1).get(0)));
    }

    /**
     * Add a StartPipe to the end of the pipeline.
     * Though, in practice, a StartPipe is usually the beginning.
     *
     * @param object the object that serves as the start of the pipeline (iterator/iterable are unfolded)
     * @return the extended FluentPipeline
     */
    public T start(final Object object) {
        return this.add(new StartPipe(object));
    }

    ///////////////////////
    /// UTILITY METHODS ///
    ///////////////////////

    /**
     * Return the number of objects iterated through the pipeline.
     *
     * @return the number of objects iterated
     */
    public long count() {
        return PipeHelper.counter(this);
    }

    /**
     * Completely drain the pipeline of its objects.
     * Useful when a sideEffect of the pipeline is desired.
     */
    public void iterate() {
        PipeHelper.iterate(this);
    }

    /**
     * Return the next X objects in the pipeline as a list.
     *
     * @param number the number of objects to return
     * @return a list of X objects (if X objects occur)
     */
    public List<E> next(final int number) {
        final List<E> list = new ArrayList<E>(number);
        PipeHelper.fillCollection(this, list, number);
        return list;
    }

    /**
     * Return a list of all the objects in the pipeline.
     *
     * @return a list of all the objects
     */
    public List<E> toList() {
        final List<E> list = new ArrayList<E>();
        PipeHelper.fillCollection(this, list);
        return list;
    }

}