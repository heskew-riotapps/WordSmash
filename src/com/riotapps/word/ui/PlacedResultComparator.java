package com.riotapps.word.ui;

import java.util.Comparator;

public class PlacedResultComparator implements Comparator<PlacedResult> {

	@Override
	public int compare(PlacedResult r1, PlacedResult r2) {
		return ((Integer)r1.getTotalPoints()).compareTo((Integer)r2.getTotalPoints());
	}

}
 