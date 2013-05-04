package com.csc591.utils;

import java.util.Comparator;

import com.csc591.DAL.Destination;

public class DurationComparator implements Comparator<Destination>{

	@Override
	public int compare(Destination o1, Destination o2) {
		// Comparison based on walking duration. Change here in case you want to sort destination list based on someother 
		// parameter.
		return o1.getWalkingTime() - o2.getWalkingTime();
	}

}
