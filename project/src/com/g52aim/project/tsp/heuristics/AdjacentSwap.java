package com.g52aim.project.tsp.heuristics;

import java.util.Random;

import com.g52aim.project.tsp.interfaces.HeuristicInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;

/**
 *
 * @author Warren G. Jackson
 *
 */
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	public AdjacentSwap(Random random) {

		super(random);
	}

	@Override
	public double apply(TSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		// TODO implementation of adjacent swap
		int loop = 0;
		if(intensityOfMutation < 0.2) {
			loop = 1;
		} else if(intensityOfMutation < 0.4) {
			loop = 2;
		} else if(intensityOfMutation < 0.6) {
			loop = 4;
		} else if(intensityOfMutation < 0.8) {
			loop = 8;
		} else if(intensityOfMutation < 1.0) {
			loop = 16;
		} else {
			loop = 32;
		}

		int numCities = solution.getNumberOfCities();
		int last = numCities - 1;
		int[] rep = solution.getSolutionRepresentation().getRepresentationOfSolution();
		double cost = solution.getObjectiveFunctionValue();

		for(int i = 0; i < loop; i++) {
			int index1 = this.random.nextInt(numCities);
			int index2;

			if(index1 == last) {
				index2 = 0;
			} else {
				index2 = index1 + 1;
			}
			int[] indexes = this.getIndexes(index1, index2, last);
			cost = this.calculateCost(rep, indexes, cost);
			rep = this.swapLocations(rep, index1, index2);
		}

		solution.getSolutionRepresentation().setRepresentationOfSolution(rep);
		solution.setObjectiveFunctionValue(cost);
		return cost;
	}


	@Override
	public boolean isCrossover() {

		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}

}
