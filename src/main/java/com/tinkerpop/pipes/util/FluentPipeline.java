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
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FluentPipeline<S, E> extends Pipeline<S, E> {

    public FluentPipeline step(final PipeClosure closure) {
        this.addPipe(new ClosurePipe(closure));
        return this;
    }

    ////////////////////
    /// BRANCH PIPES ///
    ////////////////////

    public FluentPipeline copySplit(final Pipe... pipes) {
        this.addPipe(new CopySplitPipe(pipes));
        return this;
    }

    public FluentPipeline exhaustMerge(final Pipe... pipes) {
        this.addPipe(new ExhaustMergePipe(pipes));
        return this;
    }

    public FluentPipeline exhaustMerge() {
        this.addPipe(new ExhaustMergePipe((((MetaPipe) this.removePreviousPipes(1).get(0)).getPipes())));
        return this;
    }

    public FluentPipeline fairMerge(final Pipe... pipes) {
        this.addPipe(new FairMergePipe(pipes));
        return this;
    }

    public FluentPipeline fairMerge() {
        this.addPipe(new FairMergePipe((((MetaPipe) this.removePreviousPipes(1).get(0)).getPipes())));
        return this;
    }


    public FluentPipeline ifThenElse(final PipeClosure<Boolean, Pipe> ifClosure, final PipeClosure thenClosure, final PipeClosure elseClosure) {
        this.addPipe(new IfThenElsePipe(ifClosure, thenClosure, elseClosure));
        return this;
    }

    public FluentPipeline loop(final int stepsAgo, final PipeClosure closure) {
        this.addPipe(new LoopPipe(new Pipeline(this.removePreviousPipes(stepsAgo)), closure));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    public FluentPipeline loop(final String name, final PipeClosure closure) {
        this.addPipe(new LoopPipe(new Pipeline(this.removePreviousPipes(name)), closure));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    ////////////////////
    /// FILTER PIPES ///
    ////////////////////

    public FluentPipeline andFilter(final Pipe<S, Boolean>... pipes) {
        this.addPipe(new AndFilterPipe(pipes));
        return this;
    }

    public FluentPipeline backFilter(final int numberedStep) {
        this.addPipe(new BackFilterPipe(new Pipeline(this.removePreviousPipes(numberedStep))));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    public FluentPipeline backFilter(final String namedStep) {
        this.addPipe(new BackFilterPipe(new Pipeline(this.removePreviousPipes(namedStep))));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    public FluentPipeline back(final int numberedStep) {
        return this.backFilter(numberedStep);
    }

    public FluentPipeline back(final String namedStep) {
        return this.backFilter(namedStep);
    }

    // todo: rename to UniqueObjectFilterPipe?
    public FluentPipeline duplicateFilter() {
        this.addPipe(new DuplicateFilterPipe());
        return this;
    }

    public FluentPipeline uniqueObject() {
        return this.duplicateFilter();
    }

    public FluentPipeline exceptFilter(final Collection collection) {
        this.addPipe(new ExceptFilterPipe(collection));
        return this;
    }

    public FluentPipeline except(final Collection collection) {
        return this.exceptFilter(collection);
    }

    public FluentPipeline filter(final PipeClosure<Boolean, Pipe> closure) {
        this.addPipe(new FilterClosurePipe(closure));
        return this;
    }

    // todo future filter pipe? (or remove it)

    public FluentPipeline idFilter(final Object id, final FilterPipe.Filter filter) {
        this.addPipe(new IdFilterPipe(id, filter));
        return this;
    }

    public FluentPipeline labelFilter(final String label, final FilterPipe.Filter filter) {
        this.addPipe(new LabelFilterPipe(label, filter));
        return this;
    }

    public FluentPipeline objectFilter(final Object object, final FilterPipe.Filter filter) {
        this.addPipe(new ObjectFilterPipe(object, filter));
        return this;
    }

    public FluentPipeline orFilter(final Pipe<S, Boolean>... pipes) {
        this.addPipe(new OrFilterPipe(pipes));
        return this;
    }

    public FluentPipeline propertyFilter(final String key, final FilterPipe.Filter filter, final Object value) {
        this.addPipe(new PropertyFilterPipe(key, value, filter));
        return this;
    }

    public FluentPipeline randomFilter(final Double bias) {
        this.addPipe(new RandomFilterPipe(bias));
        return this;
    }

    public FluentPipeline rangeFilter(final int low, final int high) {
        this.addPipe(new RangeFilterPipe(low, high));
        return this;
    }

    public FluentPipeline retainFilter(final Collection collection) {
        this.addPipe(new RetainFilterPipe(collection));
        return this;
    }

    public FluentPipeline retain(final Collection collection) {
        return this.retainFilter(collection);
    }

    public FluentPipeline uniquePathFilter() {
        this.addPipe(new UniquePathFilterPipe());
        return this;
    }

    public FluentPipeline uniquePath() {
        return this.uniquePathFilter();
    }

    /////////////////////////
    /// SIDE-EFFECT PIPES ///
    /////////////////////////

    public FluentPipeline aggregate(final Collection aggregate) {
        this.addPipe(new AggregatePipe(aggregate));
        return this;
    }

    public FluentPipeline aggregate(final Collection aggregate, final PipeClosure closure) {
        this.addPipe(new AggregatePipe(aggregate, closure));
        return this;
    }

    public FluentPipeline aggregate() {
        return this.aggregate(new ArrayList());
    }

    public FluentPipeline aggregate(final PipeClosure closure) {
        return this.aggregate(new ArrayList(), closure);
    }

    // todo do count pipe? (or remove it)

    public FluentPipeline groupCount(final Map<Object, Number> map, final PipeClosure<Number, Pipe>... closures) {
        if (closures.length == 0)
            this.addPipe(new GroupCountPipe(map));
        else if (closures.length == 2)
            this.addPipe(new GroupCountClosurePipe(map, closures[0], closures[1]));
        else if (closures.length == 1)
            this.addPipe(new GroupCountClosurePipe(map, closures[0], null));
        else
            throw new IllegalArgumentException("The provided closures must have a length of 0, 1, or 2");

        return this;
    }

    public FluentPipeline groupCount(final PipeClosure<Number, Pipe>... closures) {
        if (closures.length == 0)
            this.addPipe(new GroupCountPipe());
        else if (closures.length == 2)
            this.addPipe(new GroupCountClosurePipe(closures[0], closures[1]));
        else if (closures.length == 1)
            this.addPipe(new GroupCountClosurePipe(closures[0], null));
        else
            throw new IllegalArgumentException("The provided closures must have a length of 0, 1, or 2");

        return this;
    }

    public FluentPipeline optional(final int numberedStep) {
        this.addPipe(new OptionalPipe(new Pipeline(this.removePreviousPipes(numberedStep))));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    public FluentPipeline optional(final String namedStep) {
        this.addPipe(new OptionalPipe(new Pipeline(this.removePreviousPipes(namedStep))));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    public FluentPipeline sideEffect(final PipeClosure closure) {
        this.addPipe(new SideEffectClosurePipe(closure));
        return this;
    }

    public FluentPipeline table(final Table table, final Collection<String> columnNames, final PipeClosure... columnClosures) {
        this.addPipe(new TablePipe(table, columnNames, this.getAsPipes(), columnClosures));
        return this;
    }

    public FluentPipeline table(final Table table, final PipeClosure... columnClosures) {
        this.addPipe(new TablePipe(table, null, this.getAsPipes(), columnClosures));
        return this;
    }

    public FluentPipeline table(final Table table) {
        this.addPipe(new TablePipe(table, null, this.getAsPipes()));
        return this;
    }

    ///////////////////////
    /// TRANSFORM PIPES ///
    ///////////////////////

    public FluentPipeline bothEdges(final String... labels) {
        this.addPipe(new BothEdgesPipe(labels));
        return this;
    }

    public FluentPipeline bothE(final String... labels) {
        return this.bothEdges(labels);
    }

    public FluentPipeline both(final String... labels) {
        this.addPipe(new BothPipe(labels));
        return this;
    }

    public FluentPipeline bothVertices() {
        this.addPipe(new BothVerticesPipe());
        return this;
    }

    public FluentPipeline bothV() {
        return this.bothVertices();
    }

    public FluentPipeline edges() {
        this.addPipe(new EdgesPipe());
        return this;
    }

    public FluentPipeline E() {
        return this.edges();
    }

    public FluentPipeline gather() {
        this.addPipe(new GatherPipe());
        return this;
    }

    // todo has count pipe
    // todo has next pipe

    public FluentPipeline hasNext(final Pipe pipe) {
        this.addPipe(new HasNextPipe(pipe));
        return this;
    }

    public FluentPipeline idEdge(final Graph graph) {
        this.addPipe(new IdEdgePipe(graph));
        return this;
    }

    public FluentPipeline identity() {
        this.addPipe(new IdentityPipe());
        return this;
    }

    public FluentPipeline _() {
        return this.identity();
    }

    public FluentPipeline id() {
        this.addPipe(new IdPipe());
        return this;
    }

    public FluentPipeline idVertex(final Graph graph) {
        this.addPipe(new IdVertexPipe(graph));
        return this;
    }

    public FluentPipeline inEdges(final String... labels) {
        this.addPipe(new InEdgesPipe(labels));
        return this;
    }

    public FluentPipeline inE(final String... labels) {
        return this.inEdges(labels);
    }

    public FluentPipeline in(final String... labels) {
        this.addPipe(new InPipe(labels));
        return this;
    }

    public FluentPipeline inVertex() {
        this.addPipe(new InVertexPipe());
        return this;
    }

    public FluentPipeline inV() {
        return this.inVertex();
    }

    public FluentPipeline label() {
        this.addPipe(new LabelPipe());
        return this;
    }

    public FluentPipeline outEdges(final String... labels) {
        this.addPipe(new OutEdgesPipe(labels));
        return this;
    }

    public FluentPipeline outE(final String... labels) {
        return this.outEdges(labels);
    }

    public FluentPipeline out(final String... labels) {
        this.addPipe(new OutPipe(labels));
        return this;
    }

    public FluentPipeline outVertex() {
        this.addPipe(new OutVertexPipe());
        return this;
    }

    public FluentPipeline outV() {
        return this.outVertex();
    }

    public FluentPipeline path(final PipeClosure... closures) {
        if (closures.length == 0)
            this.addPipe(new PathPipe());
        else
            this.addPipe(new PathClosurePipe(closures));
        return this;
    }

    public FluentPipeline path() {
        this.addPipe(new PathPipe());
        return this;
    }

    public FluentPipeline propertyMap() {
        this.addPipe(new PropertyMapPipe());
        return this;
    }

    public FluentPipeline map() {
        return this.propertyMap();
    }

    public FluentPipeline property(final String key) {
        this.addPipe(new PropertyPipe(key));
        return this;
    }

    public FluentPipeline scatter() {
        this.addPipe(new ScatterPipe());
        return this;
    }

    public FluentPipeline sideEffectCap() {
        this.addPipe(new SideEffectCapPipe((SideEffectPipe) this.removePreviousPipes(1).get(0)));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    public FluentPipeline cap() {
        return this.sideEffectCap();
    }

    public FluentPipeline transform(final PipeClosure closure) {
        this.addPipe(new TransformClosurePipe(closure));
        return this;
    }

    public FluentPipeline vertices() {
        this.addPipe(new VerticesPipe());
        return this;
    }

    public FluentPipeline V() {
        return this.vertices();
    }


    //////////////////////
    /// UTILITY PIPES ///
    //////////////////////

    public FluentPipeline as(final String name) {
        this.addPipe(new AsPipe(name, this.removePreviousPipes(1).get(0)));
        if (this.pipes.size() == 1)
            this.setStarts(this.starts);
        return this;
    }

    public FluentPipeline start(final Object object) {
        this.addPipe(new StartPipe(object));
        return this;
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

    public List iterate(final int number) {
        final List list = new ArrayList(number);
        PipeHelper.fillCollection(this, list, number);
        return list;
    }

    public List toList() {
        final List list = new ArrayList();
        PipeHelper.fillCollection(this, list);
        return list;
    }

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

    private List<Pipe> removePreviousPipes(final int numberedStep) {
        final List<Pipe> previousPipes = new ArrayList<Pipe>();
        for (int i = this.pipes.size() - 1; i > ((this.pipes.size() - 1) - numberedStep); i--) {
            previousPipes.add(0, this.pipes.get(i));
        }
        for (int i = 0; i < numberedStep; i++) {
            this.pipes.remove(this.pipes.size() - 1);
        }
        return previousPipes;
    }

    private List<Pipe> removePreviousPipes(final String namedStep) {
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
