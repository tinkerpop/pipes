package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
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
    public FluentPipeline step(final PipeFunction function);

    public FluentPipeline step(final Pipe pipe);

    ////////////////////
    /// BRANCH PIPES ///
    ////////////////////

    /**
     * Add a CopySplitPipe to the end of the pipeline.
     *
     * @param pipes the internal pipes of the CopySplitPipe
     * @return the extended Pipeline
     */
    public FluentPipeline copySplit(final Pipe... pipes);

    /**
     * Add an ExhaustMergePipe to the end of the pipeline.
     *
     * @param pipes the internal pipes ExhaustMergePipe
     * @return the extended Pipeline
     */
    public FluentPipeline exhaustMerge(final Pipe... pipes);

    /**
     * Add an ExhaustMergePipe to the end of the pipeline.
     * The one-step previous MetaPipe in the pipeline's pipes are used as the internal pipes.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline exhaustMerge();

    /**
     * Add a FairMergePipe to the end of the pipeline.
     *
     * @param pipes the internal pipes of the FairMergePipe
     * @return the extended Pipeline
     */
    public FluentPipeline fairMerge(final Pipe... pipes);

    /**
     * Add a FairMergePipe to the end of the Pipeline.
     * The one-step previous MetaPipe in the pipeline's pipes are used as the internal pipes.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline fairMerge();

    /**
     * Add an IfThenElsePipe to the end of the Pipeline.
     *
     * @param ifFunction   the function denoting the "if" part of the pipe
     * @param thenFunction the function denoting the "then" part of the pipe
     * @param elseFunction the function denoting the "else" part of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline ifThenElse(final PipeFunction<?, Boolean> ifFunction, final PipeFunction thenFunction, final PipeFunction elseFunction);

    /**
     * Add a LoopPipe to the end of the Pipeline.
     *
     * @param numberedStep  the number of steps previous to loop back to
     * @param whileFunction the "while()" whileFunction of the LoopPipe
     * @return the extended Pipeline
     */
    public FluentPipeline loop(final int numberedStep, final PipeFunction<?, Boolean> whileFunction);

    /**
     * Add a LoopPipe ot the end of the Pipeline.
     *
     * @param namedStep     the name of the step previous to loop back to
     * @param whileFunction the "while()" function of the LoopPipe
     * @return the extended Pipeline
     */
    public FluentPipeline loop(final String namedStep, final PipeFunction<?, Boolean> whileFunction);

    /**
     * Add a LoopPipe ot the end of the Pipeline.
     *
     * @param pipe          the internal pipe of the LoopPipe
     * @param whileFunction the "while()" function of the LoopPipe
     * @return the extended Pipeline
     */
    public FluentPipeline loop(final Pipe pipe, final PipeFunction<?, Boolean> whileFunction);

    ////////////////////
    /// FILTER PIPES ///
    ////////////////////

    /**
     * Add an AndFilterPipe to the end the Pipeline.
     *
     * @param pipes the internal pipes of the AndFilterPipe
     * @return the extended Pipeline
     */
    public FluentPipeline and(final Pipe<?, Boolean>... pipes);

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to back up to
     * @return the extended Pipeline
     */
    public FluentPipeline back(final int numberedStep);

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to back up to
     * @return the extended Pipeline
     */
    public FluentPipeline back(final String namedStep);

    /**
     * Add a BackFilterPipe to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the BackFilterPipe
     * @return the extended Pipeline
     */
    public FluentPipeline back(final Pipe pipe);

    /**
     * Add a DuplicateFilterPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline dedup();

    /**
     * Add an ExceptFilterPipe to the end of the Pipeline.
     *
     * @param collection the collection except from the stream
     * @return the extended Pipeline
     */
    public FluentPipeline except(final Collection collection);

    /**
     * Add an FilterFunctionPipe to the end of the Pipeline.
     *
     * @param filterFunction the filter function of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline filter(final PipeFunction<?, Boolean> filterFunction);

    /**
     * Add an ObjectFilterPipe to the end of the Pipeline.
     *
     * @param object the object to filter on
     * @param filter the filter of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline objectFilter(final Object object, final FilterPipe.Filter filter);

    /**
     * Add an OrFilterPipe to the end the Pipeline.
     *
     * @param pipes the internal pipes of the OrFilterPipe
     * @return the extended Pipeline
     */
    public FluentPipeline or(final Pipe<S, Boolean>... pipes);

    /**
     * Add a RandomFilterPipe to the end of the Pipeline.
     *
     * @param bias the bias of the random coin
     * @return the extended Pipeline
     */
    public FluentPipeline random(final Double bias);

    /**
     * Add a RageFilterPipe to the end of the Pipeline.
     *
     * @param low  the low end of the range
     * @param high the high end of the range
     * @return the extended Pipeline
     */
    public FluentPipeline range(final int low, final int high);

    /**
     * Add a RetainFilterPipe to the end of the Pipeline.
     *
     * @param collection the collection to retain
     * @return the extended Pipeline
     */
    public FluentPipeline retain(final Collection collection);

    /**
     * Add a CyclicPathFilterPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline simplePath();

    /////////////////////////
    /// SIDE-EFFECT PIPES ///
    /////////////////////////

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     *
     * @param aggregate the collection to aggregate results into
     * @return the extended Pipeline
     */
    public FluentPipeline aggregate(final Collection aggregate);

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     *
     * @param aggregate         the collection to aggregate results into
     * @param aggregateFunction the function to run over each object prior to insertion into the aggregate
     * @return the extended Pipeline
     */
    public FluentPipeline aggregate(final Collection aggregate, final PipeFunction aggregateFunction);

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     * An ArrayList aggregate is provided.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline aggregate();

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     * An ArrayList aggregate is provided.
     *
     * @param aggregateFunction the function to run over each object prior to insertion into the aggregate
     * @return the extended Pipeline
     */
    public FluentPipeline aggregate(final PipeFunction aggregateFunction);

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to optional back to
     * @return the extended Pipeline
     */
    public FluentPipeline optional(final int numberedStep);

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to optional back to
     * @return the extended Pipeline
     */
    public FluentPipeline optional(final String namedStep);

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the OptionalPipe
     * @return the extended Pipeline
     */
    public FluentPipeline optional(final Pipe pipe);

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param map           a provided count map
     * @param keyFunction   the key function to determine map key
     * @param valueFunction the value function to determine map value
     * @return the extended Pipeline
     */
    public FluentPipeline groupCount(final Map<?, Number> map, final PipeFunction keyFunction, final PipeFunction<Number, Number> valueFunction);

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param keyFunction   the key function to determine map key
     * @param valueFunction the value function to determine map value
     * @return the extended Pipeline
     */
    public FluentPipeline groupCount(final PipeFunction keyFunction, final PipeFunction<Number, Number> valueFunction);


    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param map         a provided count map
     * @param keyFunction the key function to determine map key
     * @return the extended Pipeline
     */
    public FluentPipeline groupCount(final Map<?, Number> map, final PipeFunction keyFunction);

    /**
     * Add a GroupCountPipe or GroupCountFunctionPipe to the end of the Pipeline.
     *
     * @param keyFunction the key function to determine map key
     * @return the extended Pipeline
     */
    public FluentPipeline groupCount(final PipeFunction keyFunction);


    /**
     * Add a GroupCountPipe to the end of the Pipeline.
     *
     * @param map a provided count map
     * @return the extended Pipeline
     */
    public FluentPipeline groupCount(final Map<?, Number> map);

    /**
     * Add a GroupCountPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline groupCount();


    /**
     * Add a SideEffectFunctionPipe to the end of the Pipeline.
     *
     * @param sideEffectFunction the function of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline sideEffect(final PipeFunction sideEffectFunction);

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table           the table to fill
     * @param stepNames       the named steps to include in the filling
     * @param columnFunctions the post-processing function for each column
     * @return the extended Pipeline
     */
    public FluentPipeline table(final Table table, final Collection<String> stepNames, final PipeFunction... columnFunctions);

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table           the table to fill
     * @param columnFunctions the post-processing function for each column
     * @return the extended Pipeline
     */
    public FluentPipeline table(final Table table, final PipeFunction... columnFunctions);

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table the table to fill
     * @return the extended Pipeline
     */
    public FluentPipeline table(final Table table);

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline table();

    ///////////////////////
    /// TRANSFORM PIPES ///
    ///////////////////////


    /**
     * Add a GatherPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline gather();

    public FluentPipeline gather(PipeFunction<List, ?> function);

    /**
     * Add an IdentityPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline _();

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to memoize to
     * @return the extended Pipeline
     */
    public FluentPipeline memoize(final String namedStep);

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param numberedStep the number of the step previous to memoize to
     * @return the extended Pipeline
     */
    public FluentPipeline memoize(final int numberedStep);

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to memoize to
     * @param map       the memoization map
     * @return the extended Pipeline
     */
    public FluentPipeline memoize(final String namedStep, final Map map);

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param numberedStep the number of the step previous to memoize to
     * @param map          the memoization map
     * @return the extended Pipeline
     */
    public FluentPipeline memoize(final int numberedStep, final Map map);

    /**
     * Add a PathPipe or PathFunctionPipe to the end of the Pipeline.
     *
     * @param pathFunctions the path function of the PathFunctionPipe
     * @return the extended Pipeline
     */
    public FluentPipeline path(final PipeFunction... pathFunctions);

    /**
     * Add a PathPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline path();

    /**
     * Add a ScatterPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline scatter();

    /**
     * Add a SideEffectCapPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline sideEffectCap();

    /**
     * Add a SideEffectCapPipe to the end of the Pipeline.
     *
     * @return the extended Pipeline
     */
    public FluentPipeline cap();

    /**
     * Add a TransformFunctionPipe to the end of the Pipeline.
     *
     * @param function the transformation function of the pipe
     * @return the extended Pipeline
     */
    public FluentPipeline transform(final PipeFunction function);

    //////////////////////
    /// UTILITY PIPES ///
    //////////////////////

    /**
     * Wrap the previous step in an AsPipe
     *
     * @param name the name of the AsPipe
     * @return the extended Pipeline
     */
    public FluentPipeline as(final String name);

    /**
     * Add a StartPipe to the end of the pipeline.
     * Though, in practice, a StartPipe is usually the beginning.
     *
     * @param object the object that serves as the start of the pipeline (iterator/iterable are unfolded)
     * @return the extended Pipeline
     */
    public FluentPipeline start(final Object object);

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

}
