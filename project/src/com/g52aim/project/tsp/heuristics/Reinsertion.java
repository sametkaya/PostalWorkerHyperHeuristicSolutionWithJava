package com.g52aim.project.tsp.heuristics;

import java.util.Random;

import com.g52aim.project.tsp.interfaces.HeuristicInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;

/**
 *
 * @author Warren G. Jackson
 *
 */
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	public Reinsertion(Random random) {

		super(random);
	}

	@Override
	public double apply(TSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO implementation of reinsertion heuristic
		int loop = 0;
		if(intensityOfMutation < 0.2) {
			loop = 1;
		} else if(intensityOfMutation < 0.4) {
			loop = 2;
		} else if(intensityOfMutation < 0.6) {
			loop = 3;
		} else if(intensityOfMutation < 0.8) {
			loop = 4;
		} else if(intensityOfMutation < 1.0) {
			loop = 5;
		} else {
			loop = 6;
		}

		int numCities = solution.getNumberOfCities();
		int last = numCities - 1;
		int[] rep = solution.getSolutionRepresentation().getRepresentationOfSolution();
		double cost = solution.getObjectiveFunctionValue();

		for(int i = 0; i < loop; i++) {
			int index1 = this.random.nextInt(numCities);
			int city = rep[index1];
			int[] newR = new int[last];
			int count = 0;
			for(int j = 0; j < numCities; j++) {
				if(j != index1) {
					newR[count] = rep[j];
					count++;
				}
			}
			int[] indexes = this.getIndexes(index1, index1, last);
			cost = this.calculateCostRemove(rep, indexes, cost);

			int index2 = 0;
			boolean same = true;
			while(same) {
				index2 = this.random.nextInt(numCities);
				same = false;
				if(index2 == index1) {
					same = true;
				}
			}
			count = 0;
			for(int j = 0; j < numCities; j++) {
				if(j != index2) {
					rep[j] = newR[count];
					count++;
				} else {
					rep[j] = city;
				}
			}
			indexes = this.getIndexes(index2, index2, last);
			cost = this.calculateCostAdd(rep, indexes, cost);
		}
		solution.getSolutionRepresentation().setRepresentationOfSolution(rep);
		solution.setObjectiveFunctionValue(cost);
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
