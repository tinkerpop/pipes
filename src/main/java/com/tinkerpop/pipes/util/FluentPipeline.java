package com.tinkerpop.pipes.util;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.pipes.ClosurePipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import com.tinkerpop.pipes.branch.CopySplitPipe;
import com.tinkerpop.pipes.branch.ExhaustMergePipe;
import com.tinkerpop.pipes.branch.FairMergePipe;
import com.tinkerpop.pipes.branch.IfThenElsePipe;
import com.tinkerpop.pipes.branch.LoopPipe;
import com.tinkerpop.pipes.filter.AndFilterPipe;
import com.tinkerpop.pipes.filter.BackFilterPipe;
import com.tinkerpop.pipes.filter.DuplicateFilterPipe;
import com.tinkerpop.pipes.filter.ExceptFilterPipe;
import com.tinkerpop.pipes.filter.FilterClosurePipe;
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.filter.IdFilterPipe;
import com.tinkerpop.pipes.filter.LabelFilterPipe;
import com.tinkerpop.pipes.filter.ObjectFilterPipe;
import com.tinkerpop.pipes.filter.OrFilterPipe;
import com.tinkerpop.pipes.filter.PropertyFilterPipe;
import com.tinkerpop.pipes.filter.RandomFilterPipe;
import com.tinkerpop.pipes.filter.RangeFilterPipe;
import com.tinkerpop.pipes.filter.RetainFilterPipe;
import com.tinkerpop.pipes.filter.UniquePathFilterPipe;
import com.tinkerpop.pipes.sideeffect.AggregatePipe;
import com.tinkerpop.pipes.sideeffect.GroupCountClosurePipe;
import com.tinkerpop.pipes.sideeffect.GroupCountPipe;
import com.tinkerpop.pipes.sideeffect.OptionalPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectClosurePipe;
import com.tinkerpop.pipes.sideeffect.SideEffectPipe;
import com.tinkerpop.pipes.sideeffect.TablePipe;
import com.tinkerpop.pipes.transform.BothEdgesPipe;
import com.tinkerpop.pipes.transform.BothPipe;
import com.tinkerpop.pipes.transform.BothVerticesPipe;
import com.tinkerpop.pipes.transform.EdgesPipe;
import com.tinkerpop.pipes.transform.GatherPipe;
import com.tinkerpop.pipes.transform.HasNextPipe;
import com.tinkerpop.pipes.transform.IdEdgePipe;
import com.tinkerpop.pipes.transform.IdPipe;
import com.tinkerpop.pipes.transform.IdVertexPipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.transform.InEdgesPipe;
import com.tinkerpop.pipes.transform.InPipe;
import com.tinkerpop.pipes.transform.InVertexPipe;
import com.tinkerpop.pipes.transform.LabelPipe;
import com.tinkerpop.pipes.transform.MemoizePipe;
import com.tinkerpop.pipes.transform.OutEdgesPipe;
import com.tinkerpop.pipes.transform.OutPipe;
import com.tinkerpop.pipes.transform.OutVertexPipe;
import com.tinkerpop.pipes.transform.PathClosurePipe;
import com.tinkerpop.pipes.transform.PathPipe;
import com.tinkerpop.pipes.transform.PropertyMapPipe;
import com.tinkerpop.pipes.transform.PropertyPipe;
import com.tinkerpop.pipes.transform.ScatterPipe;
import com.tinkerpop.pipes.transform.SideEffectCapPipe;
import com.tinkerpop.pipes.transform.TransformClosurePipe;
import com.tinkerpop.pipes.transform.VerticesPipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * FluentPipeline allows for the creation of a Pipeline using the fluent-pattern (http://en.wikipedia.org/wiki/Fluent_interface ).
 * Each Pipe in Pipes maintains a fluent method in FluentPipeline. Moreover, there are two types of methods: verbose and concise.
 * Verbose methods have a longer name than their respective concise counterpart -- e.g. retainFilter vs. retain.
 * Moreover, numerous method overloading are provided in order to accommodate the various ways in which one logically thinks of a pipeline structure.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FluentPipeline<S, E> extends Pipeline<S, E> {

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
    }

    /**
     * Add an arbitrary Pipe to the end of the pipeline.
     *
     * @param pipe the pipe to concatenate to the end of the pipeline
     * @return the extended FluentPipeline
     */
    public FluentPipeline add(final Pipe pipe) {
        this.addPipe(pipe);
        return this;
    }

    /**
     * Add a ClosurePipe to the end of the pipeline.
     *
     * @param closure the closure of the ClosurePipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline step(final PipeClosure closure) {
        return this.add(new ClosurePipe(closure));
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
    public FluentPipeline copySplit(final Pipe... pipes) {
        return this.add(new CopySplitPipe(pipes));
    }

    /**
     * Add an ExhaustMergePipe to the end of the pipeline.
     *
     * @param pipes the internal pipes ExhaustMergePipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline exhaustMerge(final Pipe... pipes) {
        return this.add(new ExhaustMergePipe(pipes));
    }

    /**
     * Add an ExhaustMergePipe to the end of the pipeline.
     * The one-step previous MetaPipe in the pipeline's pipes are used as the internal pipes.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline exhaustMerge() {
        this.addPipe(new ExhaustMergePipe((((MetaPipe) this.removePreviousPipes(1).get(0)).getPipes())));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add a FairMergePipe to the end of the pipeline.
     *
     * @param pipes the internal pipes of the FairMergePipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline fairMerge(final Pipe... pipes) {
        return this.add(new FairMergePipe(pipes));
    }

    /**
     * Add a FairMergePipe to the end of the Pipeline.
     * The one-step previous MetaPipe in the pipeline's pipes are used as the internal pipes.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline fairMerge() {
        this.addPipe(new FairMergePipe((((MetaPipe) this.removePreviousPipes(1).get(0)).getPipes())));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add an IfThenElsePipe to the end of the Pipeline.
     *
     * @param ifClosure   the closure denoting the "if" part of the pipe
     * @param thenClosure the closure denoting the "then" part of the pipe
     * @param elseClosure the closure denoting the "else" part of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline ifThenElse(final PipeClosure<Boolean, Pipe> ifClosure, final PipeClosure thenClosure, final PipeClosure elseClosure) {
        return this.add(new IfThenElsePipe(ifClosure, thenClosure, elseClosure));
    }

    /**
     * Add a LoopPipe to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to loop back to
     * @param whileClosure the "while()" whileClosure of the LoopPipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline loop(final int numberedStep, final PipeClosure<Boolean, Pipe> whileClosure) {
        this.addPipe(new LoopPipe(new Pipeline(this.removePreviousPipes(numberedStep)), whileClosure));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add a LoopPipe ot the end of the Pipeline.
     *
     * @param namedStep    the name of the step previous to loop back to
     * @param whileClosure the "while()" closure of the LoopPipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline loop(final String namedStep, final PipeClosure<Boolean, Pipe> whileClosure) {
        this.addPipe(new LoopPipe(new Pipeline(this.removePreviousPipes(namedStep)), whileClosure));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add a LoopPipe ot the end of the Pipeline.
     *
     * @param pipe         the internal pipe of the LoopPipe
     * @param whileClosure the "while()" closure of the LoopPipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline loop(final Pipe pipe, final PipeClosure<Boolean, Pipe> whileClosure) {
        return this.add(new LoopPipe(pipe, whileClosure));
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
    public FluentPipeline andFilter(final Pipe<S, Boolean>... pipes) {
        return this.add(new AndFilterPipe(pipes));
    }

    /**
     * Add a BackFilter to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to back up to
     * @return the extended FluentPipeline
     */
    public FluentPipeline backFilter(final int numberedStep) {
        this.addPipe(new BackFilterPipe(new Pipeline(this.removePreviousPipes(numberedStep))));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add a BackFilter to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to back up to
     * @return the extended FluentPipeline
     */
    public FluentPipeline backFilter(final String namedStep) {
        this.addPipe(new BackFilterPipe(new Pipeline(this.removePreviousPipes(namedStep))));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add a BackFilter to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the BackFilterPipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline backFilter(final Pipe pipe) {
        return this.add(new BackFilterPipe(pipe));
    }

    /**
     * Add a BackFilter to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to back up to
     * @return the extended FluentPipeline
     */
    public FluentPipeline back(final int numberedStep) {
        return this.backFilter(numberedStep);
    }

    /**
     * Add a BackFilter to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to back up to
     * @return the extended FluentPipeline
     */
    public FluentPipeline back(final String namedStep) {
        return this.backFilter(namedStep);
    }

    /**
     * Add a BackFilter to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the BackFilterPipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline back(final Pipe pipe) {
        return this.add(new BackFilterPipe(pipe));
    }

    // todo: rename to UniqueObjectFilterPipe?
    public FluentPipeline duplicateFilter() {
        return this.add(new DuplicateFilterPipe());
    }

    public FluentPipeline uniqueObject() {
        return this.duplicateFilter();
    }

    /**
     * Add an ExceptFilter to the end of the Pipeline.
     *
     * @param collection the collection except from the stream
     * @return the extended FluentPipeline
     */
    public FluentPipeline exceptFilter(final Collection collection) {
        return this.add(new ExceptFilterPipe(collection));
    }

    /**
     * Add an ExceptFilter to the end of the Pipeline.
     *
     * @param collection the collection except from the stream
     * @return the extended FluentPipeline
     */
    public FluentPipeline except(final Collection collection) {
        return this.exceptFilter(collection);
    }

    /**
     * Add an FilterClosurePipe to the end of the Pipeline.
     *
     * @param filterClosure the filter closure of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline filter(final PipeClosure<Boolean, Pipe> filterClosure) {
        return this.add(new FilterClosurePipe(filterClosure));
    }

    // todo future filter pipe? (or remove it)

    /**
     * Add an IdFilterPipe to the end of the Pipeline.
     *
     * @param id     the id to filter on
     * @param filter the filter of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline idFilter(final Object id, final FilterPipe.Filter filter) {
        return this.add(new IdFilterPipe(id, filter));
    }

    /**
     * Add a LabelFilterPipe to the end of the Pipeline.
     *
     * @param label  the label to filter on
     * @param filter the filter of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline labelFilter(final String label, final FilterPipe.Filter filter) {
        return this.add(new LabelFilterPipe(label, filter));
    }

    /**
     * Add an ObjectFilterPipe to the end of the Pipeline.
     *
     * @param object the object to filter on
     * @param filter the filter of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline objectFilter(final Object object, final FilterPipe.Filter filter) {
        return this.add(new ObjectFilterPipe(object, filter));
    }

    /**
     * Add an OrFilterPipe to the end the Pipeline.
     *
     * @param pipes the internal pipes of the OrFilterPipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline orFilter(final Pipe<S, Boolean>... pipes) {
        return this.add(new OrFilterPipe(pipes));
    }

    /**
     * Add a PropertyFilterPipe to the end of the Pipeline.
     *
     * @param key    the property key to check
     * @param filter the filter of the pipe
     * @param value  the object to filter on
     * @return the extended FluentPipeline
     */
    public FluentPipeline propertyFilter(final String key, final FilterPipe.Filter filter, final Object value) {
        return this.add(new PropertyFilterPipe(key, value, filter));
    }

    /**
     * Add a RandomFilterPipe to the end of the Pipeline.
     *
     * @param bias the bias of the random coin
     * @return the extended FluentPipeline
     */
    public FluentPipeline randomFilter(final Double bias) {
        return this.add(new RandomFilterPipe(bias));
    }

    /**
     * Add a RandomFilterPipe to the end of the Pipeline.
     *
     * @param bias the bias of the random coin
     * @return the extended FluentPipeline
     */
    public FluentPipeline random(final Double bias) {
        return this.randomFilter(bias);
    }

    /**
     * Add a RageFilterPipe to the end of the Pipeline.
     *
     * @param low  the low end of the range
     * @param high the high end of the range
     * @return the extended FluentPipeline
     */
    public FluentPipeline rangeFilter(final int low, final int high) {
        return this.add(new RangeFilterPipe(low, high));
    }

    /**
     * Add a RageFilterPipe to the end of the Pipeline.
     *
     * @param index the index of the stream
     * @return the extended FluentPipeline
     */
    public FluentPipeline rangeFilter(final int index) {
        return this.rangeFilter(index, index);
    }

    /**
     * Add a RetainFilterPipe to the end of the Pipeline.
     *
     * @param collection the collection to retain
     * @return the extended FluentPipeline
     */
    public FluentPipeline retainFilter(final Collection collection) {
        this.addPipe(new RetainFilterPipe(collection));
        return this;
    }

    /**
     * Add a RetainFilterPipe to the end of the Pipeline.
     *
     * @param collection the collection to retain
     * @return the extended FluentPipeline
     */
    public FluentPipeline retain(final Collection collection) {
        return this.retainFilter(collection);
    }

    /**
     * Add a UniquePathFilter to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline uniquePathFilter() {
        this.addPipe(new UniquePathFilterPipe());
        return this;
    }

    /**
     * Add a UniquePathFilter to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline uniquePath() {
        return this.uniquePathFilter();
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
    public FluentPipeline aggregate(final Collection aggregate) {
        this.addPipe(new AggregatePipe(aggregate));
        return this;
    }

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     *
     * @param aggregate        the collection to aggregate results into
     * @param aggregateClosure the closure to run over each object prior to insertion into the aggregate
     * @return the extended FluentPipeline
     */
    public FluentPipeline aggregate(final Collection aggregate, final PipeClosure aggregateClosure) {
        this.addPipe(new AggregatePipe(aggregate, aggregateClosure));
        return this;
    }

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     * An ArrayList aggregate is provided.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline aggregate() {
        return this.aggregate(new ArrayList());
    }

    /**
     * Add an AggregatePipe to the end of the Pipeline.
     * An ArrayList aggregate is provided.
     *
     * @param aggregateClosure the closure to run over each object prior to insertion into the aggregate
     * @return the extended FluentPipeline
     */
    public FluentPipeline aggregate(final PipeClosure aggregateClosure) {
        return this.aggregate(new ArrayList(), aggregateClosure);
    }

    // todo do count pipe? (or remove it)

    /**
     * Add a GroupCountPipe or GroupCountClosurePipe to the end of the Pipeline.
     *
     * @param map      a provided count map
     * @param closures the key and value closures (max 2 is allowed)
     * @return the extended FluentPipeline
     */
    public FluentPipeline groupCount(final Map<Object, Number> map, final PipeClosure<Number, Pipe>... closures) {
        if (closures.length == 0)
            return this.add(new GroupCountPipe(map));
        else if (closures.length == 2)
            return this.add(new GroupCountClosurePipe(map, closures[0], closures[1]));
        else if (closures.length == 1)
            return this.add(new GroupCountClosurePipe(map, closures[0], null));
        else
            throw new IllegalArgumentException("The provided closures must have a length of 0, 1, or 2");
    }

    /**
     * Add a GroupCountPipe or GroupCountClosurePipe to the end of the Pipeline.
     * A java.util.HashMap is the constructed count map.
     *
     * @param closures the key and value closures (max 2 is allowed)
     * @return the extended FluentPipeline
     */
    public FluentPipeline groupCount(final PipeClosure<Number, Pipe>... closures) {
        if (closures.length == 0)
            return this.add(new GroupCountPipe());
        else if (closures.length == 2)
            return this.add(new GroupCountClosurePipe(closures[0], closures[1]));
        else if (closures.length == 1)
            return this.add(new GroupCountClosurePipe(closures[0], null));
        else
            throw new IllegalArgumentException("The provided closures must have a length of 0, 1, or 2");
    }

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param numberedStep the number of steps previous to optional back to
     * @return the extended FluentPipeline
     */
    public FluentPipeline optional(final int numberedStep) {
        this.addPipe(new OptionalPipe(new Pipeline(this.removePreviousPipes(numberedStep))));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to optional back to
     * @return the extended FluentPipeline
     */
    public FluentPipeline optional(final String namedStep) {
        this.addPipe(new OptionalPipe(new Pipeline(this.removePreviousPipes(namedStep))));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add an OptionalPipe to the end of the Pipeline.
     *
     * @param pipe the internal pipe of the OptionalPipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline optional(final Pipe pipe) {
        return this.add(new OptionalPipe(pipe));
    }

    /**
     * Add a SideEffectClosurePipe to the end of the Pipeline.
     *
     * @param sideEffectClosure the closure of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline sideEffect(final PipeClosure sideEffectClosure) {
        return this.add(new SideEffectClosurePipe(sideEffectClosure));
    }

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table          the table to fill
     * @param stepNames      the named steps to include in the filling
     * @param columnClosures the post-processing closures for each column
     * @return the extended FluentPipeline
     */
    public FluentPipeline table(final Table table, final Collection<String> stepNames, final PipeClosure... columnClosures) {
        return this.add(new TablePipe(table, stepNames, this.getAsPipes(), columnClosures));
    }

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table          the table to fill
     * @param columnClosures the post-processing closures for each column
     * @return the extended FluentPipeline
     */
    public FluentPipeline table(final Table table, final PipeClosure... columnClosures) {
        return this.add(new TablePipe(table, null, this.getAsPipes(), columnClosures));
    }

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @param table the table to fill
     * @return the extended FluentPipeline
     */
    public FluentPipeline table(final Table table) {
        return this.add(new TablePipe(table, null, this.getAsPipes()));
    }

    /**
     * Add a TablePipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline table() {
        return this.add(new TablePipe(new Table(), null, this.getAsPipes()));
    }

    ///////////////////////
    /// TRANSFORM PIPES ///
    ///////////////////////

    /**
     * Add a BothEdgesPipe to the end of the Pipeline.
     *
     * @param labels the edge labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline bothEdges(final String... labels) {
        return this.add(new BothEdgesPipe(labels));
    }

    /**
     * Add a BothEdgesPipe to the end of the Pipeline.
     *
     * @param labels the edges labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline bothE(final String... labels) {
        return this.bothEdges(labels);
    }

    /**
     * Add a BothPipe to the end of the Pipeline.
     *
     * @param labels the edge labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline both(final String... labels) {
        return this.add(new BothPipe(labels));
    }

    /**
     * Add a BothVerticesPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline bothVertices() {
        return this.add(new BothVerticesPipe());
    }

    /**
     * Add a BothVerticesPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline bothV() {
        return this.bothVertices();
    }

    /**
     * Add an EdgesPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline edges() {
        return this.add(new EdgesPipe());
    }

    /**
     * Add an EdgesPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline E() {
        return this.edges();
    }

    // todo: for gather -- add a TranformFilterClosure too?

    /**
     * Add a GatherPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline gather() {
        return this.add(new GatherPipe());
    }

    // todo has count pipe
    // todo has next pipe

    public FluentPipeline hasNext(final Pipe pipe) {
        this.addPipe(new HasNextPipe(pipe));
        return this;
    }

    /**
     * Add an IdEdgePipe to the end of the Pipeline.
     *
     * @param graph the graph of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline idEdge(final Graph graph) {
        return this.add(new IdEdgePipe(graph));
    }

    /**
     * Add an IdentityPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline identity() {
        return this.add(new IdentityPipe());
    }

    /**
     * Add an IdentityPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline _() {
        return this.identity();
    }

    /**
     * Add an IdPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline id() {
        return this.add(new IdPipe());
    }

    /**
     * Add an IdVertexPipe to the end of the Pipeline.
     *
     * @param graph the graph of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline idVertex(final Graph graph) {
        return this.add(new IdVertexPipe(graph));
    }

    /**
     * Add an InEdgesPipe to the end of the Pipeline.
     *
     * @param labels the edge labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline inEdges(final String... labels) {
        return this.add(new InEdgesPipe(labels));
    }

    /**
     * Add an InEdgesPipe to the end of the Pipeline.
     *
     * @param labels the edge labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline inE(final String... labels) {
        return this.inEdges(labels);
    }

    /**
     * Add a InPipe to the end of the Pipeline.
     *
     * @param labels the edge labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline in(final String... labels) {
        return this.add(new InPipe(labels));
    }

    /**
     * Add an InVertexPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline inVertex() {
        return this.add(new InVertexPipe());
    }

    /**
     * Add an InVertexPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline inV() {
        return this.inVertex();
    }

    /**
     * Add an LabelPipe to the end of the Pipeline
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline label() {
        return this.add(new LabelPipe());
    }

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to memoize to
     * @return the extended FluentPipeline
     */
    public FluentPipeline memoize(final String namedStep) {
        this.addPipe(new MemoizePipe(new Pipeline(this.removePreviousPipes(namedStep))));
        return this;
    }

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param numberedStep the number of the step previous to memoize to
     * @return the extended FluentPipeline
     */
    public FluentPipeline memoize(final int numberedStep) {
        this.addPipe(new MemoizePipe(new Pipeline(this.removePreviousPipes(numberedStep))));
        return this;
    }

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param namedStep the name of the step previous to memoize to
     * @param map       the memoization map
     * @return the extended FluentPipeline
     */
    public FluentPipeline memoize(final String namedStep, final Map map) {
        this.addPipe(new MemoizePipe(new Pipeline(this.removePreviousPipes(namedStep)), map));
        return this;
    }

    /**
     * Add a MemoizePipe to the end of the Pipeline.
     *
     * @param numberedStep the number of the step previous to memoize to
     * @param map          the memoization map
     * @return the extended FluentPipeline
     */
    public FluentPipeline memoize(final int numberedStep, final Map map) {
        this.addPipe(new MemoizePipe(new Pipeline(this.removePreviousPipes(numberedStep)), map));
        return this;
    }

    /**
     * Add an OutEdgesPipe to the end of the Pipeline.
     *
     * @param labels the edge labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline outEdges(final String... labels) {
        this.addPipe(new OutEdgesPipe(labels));
        return this;
    }

    /**
     * Add an OutEdgesPipe to the end of the Pipeline.
     *
     * @param labels the edge labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline outE(final String... labels) {
        return this.outEdges(labels);
    }

    /**
     * Add an OutPipe to the end of the Pipeline.
     *
     * @param labels the edge labels to traverse
     * @return the extended FluentPipeline
     */
    public FluentPipeline out(final String... labels) {
        return this.add(new OutPipe(labels));
    }

    /**
     * Add an OutVertexPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline outVertex() {
        return this.add(new OutVertexPipe());
    }

    /**
     * Add an OutVertexPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline outV() {
        return this.outVertex();
    }

    /**
     * Add a PathPipe or PathClosurePipe to the end of the Pipeline.
     *
     * @param pathClosures the path closures of the PathClosurePipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline path(final PipeClosure... pathClosures) {
        if (pathClosures.length == 0)
            this.addPipe(new PathPipe());
        else
            this.addPipe(new PathClosurePipe(pathClosures));
        return this;
    }

    /**
     * Add a PathPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline path() {
        this.addPipe(new PathPipe());
        return this;
    }

    /**
     * Add a PropertyMapPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline propertyMap() {
        this.addPipe(new PropertyMapPipe());
        return this;
    }

    /**
     * Add a PropertyMapPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline map() {
        return this.propertyMap();
    }

    /**
     * Add a PropertyPipe to the end of the Pipeline.
     *
     * @param key the property key
     * @return the extended FluentPipeline
     */
    public FluentPipeline property(final String key) {
        this.addPipe(new PropertyPipe(key));
        return this;
    }

    /**
     * Add a ScatterPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline scatter() {
        this.addPipe(new ScatterPipe());
        return this;
    }

    /**
     * Add a SideEffectCapPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline sideEffectCap() {
        this.addPipe(new SideEffectCapPipe((SideEffectPipe) this.removePreviousPipes(1).get(0)));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add a SideEffectCapPipe to the end of the Pipeline.
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline cap() {
        return this.sideEffectCap();
    }

    /**
     * Add a TransformClosurePipe to the end of the Pipeline.
     *
     * @param closure the transformation closure of the pipe
     * @return the extended FluentPipeline
     */
    public FluentPipeline transform(final PipeClosure closure) {
        this.addPipe(new TransformClosurePipe(closure));
        return this;
    }

    /**
     * Add a VerticesPipe to the end of the Pipeline
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline vertices() {
        return this.add(new VerticesPipe());
    }

    /**
     * Add a VerticesPipe to the end of the Pipeline
     *
     * @return the extended FluentPipeline
     */
    public FluentPipeline V() {
        return this.vertices();
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
    public FluentPipeline as(final String name) {
        this.addPipe(new AsPipe(name, this.removePreviousPipes(1).get(0)));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    /**
     * Add a StartPipe to the end of the pipeline.
     * Though, in practice, a StartPipe is usually the beginning.
     *
     * @param object the object that serves as the start of the pipeline (iterator/iterable are unfolded)
     * @return the extended FluentPipeline
     */
    public FluentPipeline start(final Object object) {
        this.addPipe(new StartPipe(object));
        return this;
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
    public List next(final int number) {
        final List list = new ArrayList(number);
        PipeHelper.fillCollection(this, list, number);
        return list;
    }

    /**
     * Return a list of all the objects in the pipeline.
     *
     * @return a list of all the objects
     */
    public List toList() {
        final List list = new ArrayList();
        PipeHelper.fillCollection(this, list);
        return list;
    }

    /**
     * Get all the AsPipes in the pipeline.
     *
     * @return all the AsPipes
     */
    public List<AsPipe> getAsPipes() {
        return this.getAsPipes(this);
    }

    private List<AsPipe> getAsPipes(final MetaPipe metaPipe) {
        List<AsPipe> asPipes = new ArrayList<AsPipe>();
        for (final Pipe subPipe : metaPipe.getPipes()) {
            if (subPipe instanceof AsPipe) {
                asPipes.add((AsPipe) subPipe);
            }
            if (subPipe instanceof MetaPipe) {
                asPipes.addAll(this.getAsPipes((MetaPipe) subPipe));
            }
        }
        return asPipes;
    }

    /**
     * A utility method to remove all the pipes back some number of steps and return them as a list.
     *
     * @param numberedStep the number of steps back to remove from the pipeline
     * @return the removed pipes
     */
    protected List<Pipe> removePreviousPipes(final int numberedStep) {
        final List<Pipe> previousPipes = new ArrayList<Pipe>();
        for (int i = this.pipes.size() - 1; i > ((this.pipes.size() - 1) - numberedStep); i--) {
            previousPipes.add(0, this.pipes.get(i));
        }
        for (int i = 0; i < numberedStep; i++) {
            this.pipes.remove(this.pipes.size() - 1);
        }
        return previousPipes;
    }

    /**
     * A utility method to remove all the pipes back some named step and return them as a list.
     *
     * @param namedStep the name of the step previous to remove from the pipeline
     * @return the removed pipes
     */
    protected List<Pipe> removePreviousPipes(final String namedStep) {
        final List<Pipe> previousPipes = new ArrayList<Pipe>();
        for (int i = this.pipes.size() - 1; i >= 0; i--) {
            final Pipe pipe = this.pipes.get(i);
            if (pipe instanceof AsPipe && ((AsPipe) pipe).getName().equals(namedStep)) {
                break;
            } else {
                previousPipes.add(0, pipe);
            }
        }
        for (int i = 0; i < previousPipes.size(); i++) {
            this.pipes.remove(this.pipes.size() - 1);
        }
        return previousPipes;
    }
}
