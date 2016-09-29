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
    // This is the elements which has taken at the last round for sorting,
    // which was only taken to check if the next element in the scopePipe changed
    S elementForNextRound;
    // The comparator which is used for sorting
    Comparator<S> comparator;

    
	public SortPipe(Comparator<S> aComparator) {
		comparator = aComparator;
	}
	
	
	public SortPipe(Comparator<S> aComparator, String aScopeName, List<AsPipe> allAsPipes) {
		comparator = aComparator;
		for (AsPipe anAsPipe : allAsPipes) {
			if (anAsPipe.getName().equals(aScopeName)) {
				scopePipe = anAsPipe;
			}
		}
	}
	

	private void fillNextIter() {
        Vector<S> vertices = new Vector<S>();
    	if (scopePipe == null) {
    		while (starts.hasNext()) {
    			S currentNode = starts.next();
    			vertices.add(currentNode);
    		}
    	} else {
    		Object lastScopeObject = scopePipe.getCurrentEnd();
    		Object currentScopeObject = lastScopeObject;
    		S currentNode = null;
    		if (elementForNextRound != null) {
    			// cause the check for the end for pulling more objects in the last round
    			// draw one object too much which is added now for the next round
    			vertices.add(elementForNextRound); 
    		}
    		while (currentScopeObject == lastScopeObject) {
    			currentNode = starts.next();
    			vertices.add(currentNode);
        		currentScopeObject = scopePipe.getCurrentEnd();
    		}
    		if (currentNode != null) {
    			elementForNextRound = currentNode;
    		}
    	}
		Collections.sort(vertices, comparator);
		iter = vertices.iterator();
	}
		
	
	
	@Override
	protected S processNextStart() throws NoSuchElementException {
		if ((iter == null) || (!iter.hasNext())) {
			fillNextIter();
		}
		return iter.next();
    }	
	
}
