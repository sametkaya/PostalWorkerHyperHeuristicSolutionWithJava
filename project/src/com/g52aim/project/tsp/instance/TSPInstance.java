package com.g52aim.project.tsp.instance;


import java.util.Random;

import com.g52aim.project.tsp.TSPObjectiveFunction;
import com.g52aim.project.tsp.interfaces.ObjectiveFunctionInterface;
import com.g52aim.project.tsp.interfaces.TSPInstanceInterface;
import com.g52aim.project.tsp.solution.SolutionRepresentation;
import com.g52aim.project.tsp.solution.TSPSolution;

/**
 *
 * @author Warren G. Jackson
 *
 * A TSP instance Object containing any necessary information
 * related to the respective problem instance.
 */
public class TSPInstance implements TSPInstanceInterface {

	private final Location[] locations;

	private final int numberOfCities;

	private final Random random;

	private ObjectiveFunctionInterface f = null;

	public TSPInstance(int numberOfCities, Location[] locations, Random random) {

		this.numberOfCities = numberOfCities;
		this.random = random;
		this.locations = locations;
	}

	/**
	 *
	 */
	@Override
	public TSPSolution createSolution(InitialisationMode mode) {
		ObjectiveFunctionInterface obj = this.getTSPObjectiveFunction();
		int[] rep = new int[this.numberOfCities];
		if(mode == InitialisationMode.RANDOM) {
			int rand = 0;
			for(int i = 0; i < this.numberOfCities; i++) {
				Boolean newR = false;
				while(!newR) {
					newR = true;
					rand = this.random.nextInt(this.numberOfCities);
					for(int j = 0; j < i; j++) {
						if(rand == rep[j]) {
							newR = false;
						}
					}
				}

				rep[i] = rand;
			}
		}
		SolutionRepresentation solR = new SolutionRepresentation(rep);
		double cost = obj.getObjectiveFunctionValue(solR);
		TSPSolution tsp = new TSPSolution(solR, cost ,this.numberOfCities);
		return tsp;
	}

	@Override
	public int getNumberOfCities() {

		return this.numberOfCities;
	}

	@Override
	public Location getLocationForCity(int cityId) {

		return locations[cityId];
	}

	@Override
	public ObjectiveFunctionInterface getTSPObjectiveFunction() {

		if(f == null) {
			this.f = new TSPObjectiveFunction(this);
		}

		return f;
	}
}
