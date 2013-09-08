package com.riotapps.word.ui;

import java.util.Comparator;

public class PlacedResultDescComparator implements Comparator<PlacedResult> {

	@Override
	public int compare(PlacedResult r1, PlacedResult r2) {
		return ((Integer)r2.getTotalPoints()).compareTo((Integer)r1.getTotalPoints());
	}
}