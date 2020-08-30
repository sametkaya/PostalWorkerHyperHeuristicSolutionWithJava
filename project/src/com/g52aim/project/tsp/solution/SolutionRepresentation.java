package com.g52aim.project.tsp.solution;

import com.g52aim.project.tsp.interfaces.SolutionRepresentationInterface;

/**
 *
 * @author Warren G. Jackson
 *
 * TODO you need to state the Object/other that will be used for representing a solution to the TSP problem.
 * 		For example, boolean[] can be used to represent the binary string for MAX-SAT problems.
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface<int[]> {

	int[] representation;
	
	public SolutionRepresentation(int[] solutionRep) {
		this.representation = solutionRep;
	}

	public SolutionRepresentationInterface<int[]> clone() {

		int[] newRep = (int[])this.representation.clone();
		return new SolutionRepresentation(newRep);
	}

	public int[] getRepresentationOfSolution(){

		return this.representation;
	}

	public void setRepresentationOfSolution(int[] solutionRepresentation) {

		this.representation = solutionRepresentation;
	}

	public int getNumberOfCities() {

		return this.representation.length;
	}

}
