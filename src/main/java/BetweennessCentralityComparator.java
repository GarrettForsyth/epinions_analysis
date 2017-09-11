package main.java;

import java.util.ArrayList;
import java.util.Comparator;

public class BetweennessCentralityComparator implements Comparator<Integer> {

	private final ArrayList<Double> bcValues;

	public BetweennessCentralityComparator(ArrayList<Double> bcValues) {
		this.bcValues = bcValues;	
	}

	public Integer[] createIndexArray(){
		Integer[] indices = new Integer[bcValues.size()];
		for(int i = 0; i < bcValues.size(); i++){
			indices[i] = i; 
		}
		return indices;
	}

	@Override
	public int compare(Integer i1, Integer i2){
		if      (bcValues.get(i1) < bcValues.get(i2)) return 1;
		else if (bcValues.get(i1) > bcValues.get(i2)) return -1;
		else                                          return 0;
	}
}
