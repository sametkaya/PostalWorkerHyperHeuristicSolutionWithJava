package com.g52aim.project.tsp.runners;

import java.awt.Color;

import com.g52aim.project.tsp.G52AIMTSP;
import com.g52aim.project.tsp.instance.Location;
import com.g52aim.project.tsp.solution.TSPSolution;
import com.g52aim.project.tsp.visualiser.TSPView;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import travelingSalesmanProblem.TSP;

/**
 * @author Warren G. Jackson
 *
 * Runs a hyper-heuristic using a default configuration then displays the best solution found.
 */
public abstract class HH_Runner_Visual {

	public HH_Runner_Visual() {

	}

	public void run() {

		long seed = 15032018l;
		long timeLimit = 60_000l;
		ProblemDomain problem = new TSP(seed);
		problem.loadInstance(4);
		HyperHeuristic hh = getHyperHeuristic(seed);
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(problem);
		hh.run();

		System.out.println("f(s_best) = " + hh.getBestSolutionValue());
		Location[] route = transformSolution((TSPSolution)((G52AIMTSP)problem).getBestSolution(), (G52AIMTSP)problem);
		new TSPView(route, Color.RED, Color.GREEN);
	}

	/**
	 * Transforms the best solution found, represented as a TSPSolution, into an ordering of Location's
	 * which the visualiser tool uses to draw the tour.
	 */
	protected Location[] transformSolution(TSPSolution solution, G52AIMTSP problem) {

		// TODO you will need to transform your representation of a solution into an ordered array of Locations
		int[] solArr = solution.getSolutionRepresentation().getRepresentationOfSolution();
		Location[] route = new Location[solution.getNumberOfCities()];
		for(int i = 0; i < solution.getNumberOfCities(); i++) {
			route[i] = problem.instance.getLocationForCity(solArr[i]);
		}
		return route;
	}

	/**
	 * Allows a general visualiser runner by making the HyperHeuristic abstract.
	 * You can sub-class this class to run any hyper-heuristic that you want.
	 */
	protected abstract HyperHeuristic getHyperHeuristic(long seed);
}
