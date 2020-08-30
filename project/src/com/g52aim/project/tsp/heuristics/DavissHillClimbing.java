package com.g52aim.project.tsp.heuristics;

import java.util.Random;

import com.g52aim.project.tsp.interfaces.HeuristicInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;


/**
 *
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {

	public DavissHillClimbing(Random random) {

		super(random);
	}

	@Override
	public double apply(TSPSolutionInterface solution, double dos, double iom) {

		// TODO implement Davis's Hill Climbing using adjacent swaps
		int x = 0;
		if(dos < 0.2) {
			x = 1;
		} else if(dos < 0.4) {
			x = 2;
		} else if(dos < 0.6) {
			x = 3;
		} else if(dos < 0.8) {
			x = 4;
		} else if(dos < 1.0) {
			x = 5;
		} else {
			x = 6;
		}
		int cityNum = solution.getNumberOfCities();
		double cost = solution.getObjectiveFunctionValue();
		while(x > 0) {
			x--;
			int[] perm = new int[cityNum];

			for(int i = 0; i < cityNum; i++) {
				boolean exists = true;
				while(exists) {
					exists = false;
					perm[i] = this.random.nextInt(cityNum);
					for(int j = 0; j < i; j++) {
						if(perm[i] == perm[j]) {
							exists = true;
							break;
						}
					}
				}
			}

			for(int i = 0; i < cityNum; i++) {
				TSPSolutionInterface solutionBackup = solution.clone();

				double candidate = this.applyAdjacentSwap(solutionBackup, perm[i]);

				if (candidate <= cost) {
					cost = candidate;
					solution.getSolutionRepresentation().setRepresentationOfSolution(solutionBackup.getSolutionRepresentation().getRepresentationOfSolution());
					solution.setObjectiveFunctionValue(cost);
				}
			}
		}
		return cost;
	}

	/*
	 * TODO update the methods below to return the correct boolean value.
	 */

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
