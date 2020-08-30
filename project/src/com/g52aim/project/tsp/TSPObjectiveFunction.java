package com.g52aim.project.tsp;

import com.g52aim.project.tsp.instance.Location;
import com.g52aim.project.tsp.interfaces.ObjectiveFunctionInterface;
import com.g52aim.project.tsp.interfaces.TSPInstanceInterface;
import com.g52aim.project.tsp.solution.SolutionRepresentation;

/**
 *
 * @author Warren G. Jackson
 *
 */
public class TSPObjectiveFunction implements ObjectiveFunctionInterface {

	private final TSPInstanceInterface instance;

	public TSPObjectiveFunction(TSPInstanceInterface instance) {

		this.instance = instance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentation solution) {

		double cost = 0.0;

		int[] rep = solution.getRepresentationOfSolution();
		for(int i = 0; i < rep.length - 1; i++) {
			cost = cost + this.getCost(rep[i], rep[i + 1]);
		}
		cost = cost + this.getCost(rep[rep.length - 1], rep[0]);
		return cost;
	}

	@Override
	public double getCost(int location_a, int location_b) {
		Location a = this.instance.getLocationForCity(location_a);
		Location b = this.instance.getLocationForCity(location_b);

		double x_a = a.getX();
		double x_b = b.getX();

		double y_a = a.getY();
		double y_b = b.getY();

		double x = Math.pow((x_a-x_b), 2);
		x = x + Math.pow((y_a-y_b), 2);

		return Math.round(Math.sqrt(x));
	}

}
