package com.g52aim.project.tsp;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.g52aim.project.tsp.heuristics.AdjacentSwap;
import com.g52aim.project.tsp.heuristics.DavissHillClimbing;
import com.g52aim.project.tsp.heuristics.NextDescent;
import com.g52aim.project.tsp.heuristics.OX;
import com.g52aim.project.tsp.heuristics.PMX;
import com.g52aim.project.tsp.heuristics.Reinsertion;
import com.g52aim.project.tsp.heuristics.TwoOpt;
import com.g52aim.project.tsp.hyperheuristics.InitialisationTest_HH;
import com.g52aim.project.tsp.hyperheuristics.PSYCY_HH;
import com.g52aim.project.tsp.hyperheuristics.SR_IE_HH;
import com.g52aim.project.tsp.instance.InitialisationMode;
import com.g52aim.project.tsp.instance.Location;
import com.g52aim.project.tsp.instance.TSPInstance;
import com.g52aim.project.tsp.instance.reader.TSPInstanceReader;
import com.g52aim.project.tsp.interfaces.HeuristicInterface;
import com.g52aim.project.tsp.interfaces.TSPInstanceInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;
import com.g52aim.project.tsp.interfaces.XOHeuristicInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

/**
 *
 * @author Warren G. Jackson
 *
 */
public class G52AIMTSP extends ProblemDomain {

	// TODO create an array of the instance file names; e.g. "dj38".
	private String[] instanceFiles = {"circle", "dj38", "plus","square", "T81", "u724"};
	public TSPInstanceInterface instance;
	private TSPSolutionInterface bestSolution;
	private TSPSolutionInterface[] memory = new TSPSolutionInterface[2];

	public G52AIMTSP(long seed) {

		super(seed);
	}

	public TSPSolutionInterface getSolution(int index) {

		return this.memory[index];
	}

	public TSPSolutionInterface getBestSolution() {

		return this.bestSolution;
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		HeuristicInterface as = null;

		this.copySolution(currentIndex, candidateIndex);
		double cand = this.memory[candidateIndex].getObjectiveFunctionValue();

		as.setObjectiveFunction(this.instance.getTSPObjectiveFunction());
		cand = as.apply(this.memory[candidateIndex], this.depthOfSearch, this.intensityOfMutation);

		if(bestSolution == null || cand < this.bestSolution.getObjectiveFunctionValue()) {
			this.bestSolution = memory[candidateIndex].clone();
		}
		return cand;
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		HeuristicInterface as = null;
		XOHeuristicInterface xoas = null;
		boolean bool = false;

		if(this.memory[candidateIndex] == null) {
			this.copySolution(parent1Index, candidateIndex);
		}

		double cand = this.memory[candidateIndex].getObjectiveFunctionValue();
		if(bool) {
			xoas.setObjectiveFunction(this.instance.getTSPObjectiveFunction());
			cand = xoas.apply(this.memory[parent1Index], this.memory[parent2Index], this.memory[candidateIndex], this.depthOfSearch, this.intensityOfMutation);

		} else {
			this.copySolution(parent1Index, candidateIndex);
			as.setObjectiveFunction(this.instance.getTSPObjectiveFunction());
			cand = as.apply(this.memory[candidateIndex], this.depthOfSearch, this.intensityOfMutation);
		}

		if(bestSolution == null || cand < this.bestSolution.getObjectiveFunctionValue()) {
			this.bestSolution = memory[candidateIndex].clone();
		}
		return cand;
	}

	@Override
	public String bestSolutionToString() {

		return getBestSolution().toString();
	}

	@Override
	public boolean compareSolutions(int a, int b) {
		int[] root1 = this.memory[a].getSolutionRepresentation().getRepresentationOfSolution();
		int[] root2 = this.memory[b].getSolutionRepresentation().getRepresentationOfSolution();
		for(int i = 0; i< this.memory[a].getNumberOfCities(); i++) {
			if(root1[i] != root2[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void copySolution(int a, int b) {
		this.memory[b] = this.memory[a].clone();
	}

	@Override
	public double getBestSolutionValue() {
		return this.bestSolution.getObjectiveFunctionValue();
	}

	@Override
	public double getFunctionValue(int index) {
		return this.memory[index].getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		if (type == HeuristicType.MUTATION) {
			return new int[] {0,1,2};
		} else if(type == HeuristicType.LOCAL_SEARCH) {
			return new int[] {3,4};
		} else if(type == HeuristicType.CROSSOVER) {
			return new int[] {5,6};
		} else {
			return null;
		}
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {

		return new int[] {3,4};
	}
	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {

		return new int[] {0,1,2,5,6};
	}

	@Override
	public int getNumberOfHeuristics() {

		return 7;
	}

	@Override
	public int getNumberOfInstances() {

		return this.instanceFiles.length;
	}

	@Override
	public void initialiseSolution(int index) {

		this.memory[index] = this.instance.createSolution(InitialisationMode.RANDOM);

		if(bestSolution == null || this.memory[index].getObjectiveFunctionValue() < this.bestSolution.getObjectiveFunctionValue()){
			this.bestSolution = memory[index].clone();
		}

	}

	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "tsp" + SEP + instanceFiles [instanceId] + ".tsp";

		// TODO create instance reader and problem instance
		TSPInstanceReader reader = new TSPInstanceReader();
		this.instance = reader.readTSPInstance(Paths.get(instanceName), this.rng);


		// TODO set objective function in heuristics


	}

	@Override
	public void setMemorySize(int size) {
		TSPSolutionInterface[] tempMemory = new TSPSolutionInterface[size];
		if(this.memory != null) {
			if(this.memory.length < size) {
				for(int i = 0; i < this.memory.length; i++) {
					tempMemory[i] = memory[i];
				}
			} else {
				for(int i = 0; i < size; i++) {
					tempMemory[i] = memory[i];
				}
			}
		}
		this.memory = tempMemory;
	}

	@Override
	public String solutionToString(int index) {

		return this.memory[index].toString();
	}

	@Override
	public String toString() {

		String username = "psy___";
		return username +  "'s TSP";
	}

	/**
	 * You can use this for testing :)
	 *
	 * @param args
	 */
	public static void main(String [] args) {

		long seed = 527893l;
		long timeLimit = 60_000;
		G52AIMTSP tsp = new G52AIMTSP(seed);
		HyperHeuristic hh = new PSYCY_HH(seed);
		tsp.loadInstance( 4 );
		tsp.setMemorySize(2);

		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(tsp);
		hh.run();
		double best = hh.getBestSolutionValue();
		System.out.println(best);

		// TODO you will need to populate this based on your representation!
		List<Location> routeLocations = new ArrayList<>();
		TSPSolutionInterface bestSol = tsp.getBestSolution();
		int[] solutionArr = bestSol.getSolutionRepresentation().getRepresentationOfSolution();
		for(int i = 0; i < bestSol.getNumberOfCities(); i++) {
			routeLocations.add(tsp.instance.getLocationForCity(solutionArr[i]));
		}
		SolutionPrinter.printSolution(routeLocations);
	}
}
