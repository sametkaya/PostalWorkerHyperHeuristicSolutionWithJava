package com.g52aim.project.tsp.heuristics;

import java.util.Random;

import com.g52aim.project.tsp.interfaces.ObjectiveFunctionInterface;
import com.g52aim.project.tsp.solution.SolutionRepresentation;

/**
 *
 * @author Warren G. Jackson
 *
 * TODO you can add any common functionality here
 *		to save having to re-implement them in all
 *		your other heuristics!
 *
 *
 * If this this concept it new to you, you may want
 * to read this article on inheritance:
 * https://www.tutorialspoint.com/java/java_inheritance.htm
 */
public class CrossoverHeuristicOperators {

	protected final Random random;
	protected ObjectiveFunctionInterface obj;

	public CrossoverHeuristicOperators(Random random) {

		this.random = random;
	}


	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		// TODO store the objective function so we can use it later!
		this.obj = f;
	}

	public int[] getCutPoints(int numCities) {
		int[] cuts = new int[2];
		cuts[0] = this.random.nextInt(numCities);
		boolean same = true;
		cuts[1] = 0;
		while(same) {
			same = false;
			cuts[1] = this.random.nextInt(numCities);
			if(cuts[1] == cuts[0]) {
				same = true;
			}
		}
		if(cuts[0] > cuts[1]) {
			int temp = cuts[1];
			cuts[1] = cuts[0];
			cuts[0] = temp;
		}
		return cuts;
	}

	public int[] getSegment(int[] cuts, int[] r) {
		int[] s = new int[cuts[1] - cuts[0]];
		for(int i = cuts[0]; i < cuts[1]; i++) {
			s[i - cuts[0]] = r[i];
		}
		return s;
	}

	public int[] getRest(int[] cuts, int[] r, int s[], int numCities) {
		int length = numCities - (cuts[1] - cuts[0]);
		int[] rest = new int[length];
		int count = 0;
		for(int i = 0; i < numCities; i++) {
			boolean unused = true;
			for(int k = 0; k < s.length; k++) {
				if(s[k] == r[(i + cuts[1]) % numCities]) {
					unused = false;
					break;
				}
			}
			if(unused) {
				rest[count] = r[(i + cuts[1]) % numCities];
				count++;
			}
		}
		return rest;
	}

	public double getCostRoute(int[] route) {
		SolutionRepresentation sr = new SolutionRepresentation(route);
		double cost = this.obj.getObjectiveFunctionValue(sr);
		return cost;
	}

	public int[] completeRoute(int[] repO, int[] repP, int start, int end, int[] sol1, int[] sol2) {
		for(int i = start; i < end; i++) {
			boolean exists = false;
			int position = -1;
			for(int j = 0; j < sol1.length; j++) {
				if(repP[i] == sol1[j]) {
					position = j;
					exists = true;
					break;
				}
			}

			if(exists) {
				repO[i] = sol2[position];
			} else {
				repO[i] = repP[i];
			}
		}
		return repO;
	}
}
