package com.csc591.utils;

import java.util.Comparator;

import com.csc591.DAL.Destination;

public class DurationComparator implements Comparator<Destination>{

	@Override
	public int compare(Destination o1, Destination o2) {
		
		return o1.getWalkingTime() - o2.getWalkingTime();
	}
	

}
