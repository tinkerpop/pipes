package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.branch.util.LoopBundle;
import com.tinkerpop.pipes.filter.FilterPipe;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface FluentPipeline<S, E> {

    /**
     * Add a FunctionPipe to the end of the pipeline.
     *
     * @param function the function of the FunctionPipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> step(final PipeFunction function);

    public <T> FluentPipeline<S, T> step(final Pipe<?, T> pipe);

    ////////////////////
    /// BRANCH PIPES ///
    ////////////////////

    /**
     * Add a CopySplitPipe to the end of the pipeline.
     *
     * @param pipes the internal pipes of the CopySplitPipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> copySplit(final Pipe... pipes);

    /**
     * Add an ExhaustMergePipe to the end of the pipeline.
     *
     * @param pipes the internal pipes ExhaustMergePipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> exhaustMerge(final Pipe... pipes);

    /**
     * Add an ExhaustMergePipe to the end of the pipeline.
     * The one-step previous MetaPipe in the pipeline's pipes are used as the internal pipes.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> exhaustMerge();

    /**
     * Add a FairMergePipe to the end of the pipeline.
     *
     * @param pipes the internal pipes of the FairMergePipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> fairMerge(final Pipe... pipes);

    /**
     * Add a FairMergePipe to the end of the Pipeline.
     * The one-step previous MetaPipe in the pipeline's pipes are used as the internal pipes.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> fairMerge();

    /**
     * Add an IfThenElsePipe to the end of the Pipeline.
     *
     * @param ifFunction   the function denoting the "if" part of the pipe
     * @param thenFunction the function denoting the "then" part of the pipe
     * @param elseFunction the function denoting the "else" part of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> ifThenElse(final PipeFunction<?, Boolean> ifFunction, final PipeFunction thenFunction, final PipeFunction elseFunction);

    public FluentPipeline<S, S> loop(final PipeFunction<LoopBundle<S>, Boolean> whileFunction, final PipeFunction<?, Pipe<S, S>> pipeFunction);

    public FluentPipeline<S, S> loop(final PipeFunction<LoopBundle<S>, Boolean> whileFunction, final PipeFunction<?, Pipe<S, S>> pipeFunction, final PipeFunction<LoopBundle<S>, Boolean> emitFunction);


    ////////////////////
    /// FILTER PIPES ///
    ////////////////////

    /**
     * Add an AndFilterPipe to the end the Pipeline.
     *
     * @param pipes the internal pipes of the AndFilterPipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> and(final Pipe<?, Boolean>... pipes);

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to back up to
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> back(final int numberedStep);

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to back up to
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> back(final String namedStep);

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the BackFilterPipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> back(final Pipe pipe);

    /**
     * Add a DuplicateFilterPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> dedup();

    /**
     * Add an ExceptFilterPipe to the end of the Pipeline.
     *
     * @param collection the collection except from the stream
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> except(final Collection collection);

    /**
     * Add an FilterFunctionPipe to the end of the Pipeline.
     *
     * @param filterFunction the filter function of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> filter(final PipeFunction<?, Boolean> filterFunction);

    /**
     * Add an ObjectFilterPipe to the end of the Pipeline.
     *
     * @param object the object to filter on
     * @param filter the filter of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> objectFilter(final Object object, final FilterPipe.Filter filter);

    /**
     * Add an OrFilterPipe to the end the Pipeline.
     *
     * @param pipes the internal pipes of the OrFilterPipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> or(final Pipe<S, Boolean>... pipes);

    /**
     * Add a RandomFilterPipe to the end of the Pipeline.
     *
     * @param bias the bias of the random coin
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> random(final Double bias);

    /**
     * Add a RageFilterPipe to the end of the Pipeline.
     *
     * @param low  the low end of the range
     * @param high the high end of the range
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> range(final int low, final int high);

    /**
     * Add a RetainFilterPipe to the end of the Pipeline.
     *
     * @param collection the collection to retain
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> retain(final Collection collection);

    /**
     * Add a CyclicPathFilterPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> simplePath();

    /////////////////////////
    /// SIDE-EFFECT PIPES ///
    /////////////////////////

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     *
     * @param aggregate the collection to aggregate results into
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> aggregate(final Collection aggregate);

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     *
     * @param aggregate         the collection to aggregate results into
     * @param aggregateFunction the function to run over each object prior to insertion into the aggregate
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> aggregate(final Collection aggregate, final PipeFunction aggregateFunction);

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     * An ArrayList aggregate is provided.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> aggregate();

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     * An ArrayList aggregate is provided.
     *
     * @param aggregateFunction the function to run over each object prior to insertion into the aggregate
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> aggregate(final PipeFunction aggregateFunction);

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to optional back to
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> optional(final int numberedStep);

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to optional back to
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> optional(final String namedStep);

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the OptionalPipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> optional(final Pipe pipe);

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param map           a provided count map
     * @param keyFunction   the key function to determine map key
     * @param valueFunction the value function to determine map value
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> groupCount(final Map<?, Number> map, final PipeFunction keyFunction, final PipeFunction<Number, Number> valueFunction);

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param keyFunction   the key function to determine map key
     * @param valueFunction the value function to determine map value
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> groupCount(final PipeFunction keyFunction, final PipeFunction<Number, Number> valueFunction);


    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param map         a provided count map
     * @param keyFunction the key function to determine map key
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> groupCount(final Map<?, Number> map, final PipeFunction keyFunction);

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param keyFunction the key function to determine map key
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> groupCount(final PipeFunction keyFunction);


    /**
     * Add a GroupCountPipe to the end of the Pipeline.
     *
     * @param map a provided count map
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> groupCount(final Map<?, Number> map);

    /**
     * Add a GroupCountPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> groupCount();


    /**
     * Add a SideEffectFunctionPipe to the end of the Pipeline.
     *
     * @param sideEffectFunction the function of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> sideEffect(final PipeFunction sideEffectFunction);

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table           the table to fill
     * @param stepNames       the named steps to include in the filling
     * @param columnFunctions the post-processing function for each column
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> table(final Table table, final Collection<String> stepNames, final PipeFunction... columnFunctions);

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table           the table to fill
     * @param columnFunctions the post-processing function for each column
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> table(final Table table, final PipeFunction... columnFunctions);

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table the table to fill
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> table(final Table table);

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> table();

    ///////////////////////
    /// TRANSFORM PIPES ///
    ///////////////////////


    /**
     * Add a GatherPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, List<?>> gather();

    public FluentPipeline<S, ?> gather(PipeFunction<List, ?> function);

    /**
     * Add an IdentityPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> _();

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to memoize to
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> memoize(final String namedStep);

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param numberedStep the number of the step previous to memoize to
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> memoize(final int numberedStep);

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to memoize to
     * @param map       the memoization map
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> memoize(final String namedStep, final Map map);

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param numberedStep the number of the step previous to memoize to
     * @param map          the memoization map
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> memoize(final int numberedStep, final Map map);

    /**
     * Add a PathPipe or PathFunctionPipe to the end of the Pipeline.
     *
     * @param pathFunctions the path function of the PathFunctionPipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, List<?>> path(final PipeFunction... pathFunctions);

    /**
     * Add a ScatterPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> scatter();

    /**
     * Add a SideEffectCapPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> cap();

    /**
     * Add a TransformFunctionPipe to the end of the Pipeline.
     *
     * @param function the transformation function of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, ?> transform(final PipeFunction function);

    //////////////////////
    /// UTILITY PIPES ///
    //////////////////////

    /**
     * Wrap the previous step in an AsPipe
     *
     * @param name the name of the AsPipe
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> as(final String name);

    /**
     * Add a StartPipe to the end of the pipeline.
     * Though, in practice, a StartPipe is usually the beginning.
     *
     * @param object the object that serves as the start of the pipeline (iterator/iterable are unfolded)
     * @return the extended Pipeline
     */
    public FluentPipeline<S, E> start(final Object object);

    ///////////////////////
    /// UTILITY METHODS ///
    ///////////////////////

    /**
     * Return the number of objects iterated through the pipeline.
     *
     * @return the number of objects iterated
     */
    public long count();

    /**
     * Completely drain the pipeline of its objects.
     * Useful when a sideEffect of the pipeline is desired.
     */
    public void iterate();

    /**
     * Return the next X objects in the pipeline as a list.
     *
     * @param number the number of objects to return
     * @return a list of X objects (if X objects occur)
     */
    public List<E> next(final int number);

    /**
     * Return a list of all the objects in the pipeline.
     *
     * @return a list of all the objects
     */
    public List<E> toList();

    /**
     * Fill the provided collection with the objects in the pipeline.
     *
     * @param collection the collection to fill
     * @return the collection filled
     */
    public Collection<E> fill(final Collection<E> collection);

}
