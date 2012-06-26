package com.tinkerpop.pipes.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import com.tinkerpop.pipes.AbstractPipe;

public class SortPipe<S> extends AbstractPipe<S, S> {

	Iterator<S> iter;
	// This is the scope to go back for collecting elements for sorting
    AsPipe scopePipe;
    // The comparator which is used for sorting
    Comparator<S> comparator;

    
	public SortPipe(Comparator<S> aComparator) {
		comparator = aComparator;
	}
	
	
	public SortPipe(Comparator<S> aComparator, AsPipe asPipe) {
		comparator = aComparator;
		scopePipe = asPipe;
		asPipe.referencedSortPipe.add(this);
	}
	

	public boolean isCollectingNewElements() {
		return collectingNewElements;
	}
	
	
	boolean collectingNewElements = false;
	private void fillNextIter() {
        Vector<S> vertices = new Vector<S>();
    	if (scopePipe == null) {
    		while (starts.hasNext()) {
    			S currentNode = starts.next();
    			vertices.add(currentNode);
    		}
    	} else {
			S currentNode = starts.next();
			vertices.add(currentNode);
			// das darf erst hier auf true gesetzt werden, 
			// da ansosnten die As-Pipe niemals ein weiteres Element
			// ausspucken würde.
			collectingNewElements = true;
    		while (starts.hasNext()) {
    			currentNode = starts.next();
    			vertices.add(currentNode);
    		}
    		try {
    			currentNode = starts.next();
    			vertices.add(currentNode);    			
    		} catch (NoSuchElementException exp) {    			
    		}
    	}
		Collections.sort(vertices, comparator);
		iter = vertices.iterator();
		collectingNewElements = false;
	}
		
		
	@Override
    public boolean hasNext() {
		if ((iter != null) && (iter.hasNext())) {
			return true;
		}
		return starts.hasNext();
    }
    
    
	@Override
	public void reset() {
		iter = null;
		super.reset();
	}


	@Override
	protected S processNextStart() throws NoSuchElementException {		
		if ((iter == null) || !iter.hasNext()) {
			fillNextIter();
		}
		S nextFromIter = iter.next();
		return nextFromIter;
	}
	
	
}
