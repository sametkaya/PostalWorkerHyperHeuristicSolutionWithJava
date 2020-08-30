package com.g52aim.project.tsp.heuristics;

import java.util.Random;

import com.g52aim.project.tsp.interfaces.ObjectiveFunctionInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;


/**
 *
 * @author Warren G. Jackson
 *
 * TODO you can add any common functionality here
 *		to save having to re-implement them in all
 *		your other heuristics!
 *		( swapping two cities seems to be popular )
 *
 *
 * If this this concept it new to you, you may want
 * to read this article on inheritance:
 * https://www.tutorialspoint.com/java/java_inheritance.htm
 */
public class HeuristicOperators {

	protected final Random random;
	protected ObjectiveFunctionInterface obj;

	public HeuristicOperators(Random random) {

		this.random = random;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		// TODO store the objective function so we can use it later!
		this.obj = f;
	}
	public int[] swapLocations(int[] locations, int index1, int index2) {
		int temp = locations[index2];
		locations[index2] = locations[index1];
		locations[index1] = temp;
		return locations;
	}

	public int[] getIndexes(int index1, int index2, int last) {
		int[] intArr = new int[4];
		intArr[1] = index1;
		intArr[2] = index2;
		if(index1 == 0) {
			intArr[0] = last;
		} else {
			intArr[0] = index1 - 1;
		}
		if(index2 == last) {
			intArr[3] = 0;
		} else {
			intArr[3] = index2 + 1;
		}
		return intArr;
	}

	public double calculateCost(int[] rep, int[] indexes, double cost) {
		cost = cost - this.obj.getCost(rep[indexes[0]], rep[indexes[1]]) - this.obj.getCost(rep[indexes[2]], rep[indexes[3]]);
		cost = cost + this.obj.getCost(rep[indexes[0]], rep[indexes[2]]) + this.obj.getCost(rep[indexes[1]], rep[indexes[3]]);
		return cost;
	}

	public double calculateCostRemove(int[] rep, int[] indexes, double cost) {
		cost = cost - this.obj.getCost(rep[indexes[0]], rep[indexes[1]]) - this.obj.getCost(rep[indexes[2]], rep[indexes[3]]);
		cost = cost + this.obj.getCost(rep[indexes[0]], rep[indexes[3]]);
		return cost;
	}

	public double calculateCostAdd(int[] r, int[] indexes, double cost) {
		cost = cost - this.obj.getCost(r[indexes[0]], r[indexes[3]]);
		cost = cost + this.obj.getCost(r[indexes[0]], r[indexes[1]]) + this.obj.getCost(r[indexes[2]], r[indexes[3]]);
		return cost;
	}

	public double applyAdjacentSwap(TSPSolutionInterface solution, int index1) {
		int numCities = solution.getNumberOfCities();
		int last = numCities - 1;
		int[] r = solution.getSolutionRepresentation().getRepresentationOfSolution();
		double cost = solution.getObjectiveFunctionValue();
		int index2;

		if(index1 == last) {
			index2 = 0;
		} else {
			index2 = index1 + 1;
		}
		int[] indexes = this.getIndexes(index1, index2, last);
		cost = this.calculateCost(r, indexes, cost);
		r = this.swapLocations(r, index1, index2);

		solution.getSolutionRepresentation().setRepresentationOfSolution(r);
		solution.setObjectiveFunctionValue(cost);
		return cost;
	}
}
