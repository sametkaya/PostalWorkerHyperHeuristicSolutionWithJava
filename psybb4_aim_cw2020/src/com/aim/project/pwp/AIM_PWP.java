package com.aim.project.pwp;

import java.awt.Color;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import com.aim.project.pwp.heuristics.AdjacentSwap;
import com.aim.project.pwp.heuristics.CX;
import com.aim.project.pwp.heuristics.DavissHillClimbing;
import com.aim.project.pwp.heuristics.InversionMutation;
import com.aim.project.pwp.heuristics.NextDescent;
import com.aim.project.pwp.heuristics.OX;
import com.aim.project.pwp.heuristics.Reinsertion;
import com.aim.project.pwp.instance.InitialisationMode;
import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.reader.PWPInstanceReader;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.Visualisable;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.pwp.visualiser.PWPView;

import AbstractClasses.ProblemDomain;

public class AIM_PWP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = { "square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446",
			"streetlights-35714" };

	private PWPSolutionInterface[] aoMemoryOfSolutions = new PWPSolutionInterface[2];

	public PWPSolutionInterface oBestSolution;

	public PWPInstanceInterface oInstance;

	private HeuristicInterface[] aoHeuristics;

	private ObjectiveFunctionInterface oObjectiveFunction;

	private final long seed;

	public AIM_PWP(long seed) {

		// TODO - set default memory size and create the array of low-level heuristics

		super(seed);
		this.seed = seed;

		setMemorySize(2);
		aoHeuristics = new HeuristicInterface[7];

		// Mutation
		aoHeuristics[0] = new InversionMutation(super.rng);
		aoHeuristics[1] = new AdjacentSwap(super.rng);
		aoHeuristics[2] = new Reinsertion(super.rng);
		// LocalSearch
		aoHeuristics[3] = new NextDescent(super.rng);
		aoHeuristics[4] = new DavissHillClimbing(super.rng);
		// Crossover
		aoHeuristics[5] = new CX(super.rng);
		aoHeuristics[6] = new OX(super.rng);
		
	}

	public PWPSolutionInterface getSolution(int index) {
		// TODO
		return aoMemoryOfSolutions[index];
	}

	public PWPSolutionInterface getBestSolution() {
		// TODO
		return oBestSolution;
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		// TODO - apply heuristic and return the objective value of the candidate
		// solution
		// remembering to keep track/update the best solution
		double candidateObjectiveVal;
		switch (hIndex) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			aoHeuristics[hIndex].apply(this.aoMemoryOfSolutions[candidateIndex], getDepthOfSearch(),
					getIntensityOfMutation());
			this.aoMemoryOfSolutions[candidateIndex].setObjectiveFunctionValue(this.oObjectiveFunction
					.getObjectiveFunctionValue(aoMemoryOfSolutions[candidateIndex].getSolutionRepresentation()));
			candidateObjectiveVal = this.aoMemoryOfSolutions[candidateIndex].getObjectiveFunctionValue();
			break;
		case 5:
		case 6:
			PWPSolutionInterface child = this.oInstance.createSolution(InitialisationMode.RANDOM);
			((XOHeuristicInterface) (aoHeuristics[hIndex])).apply(this.aoMemoryOfSolutions[currentIndex],
					this.aoMemoryOfSolutions[candidateIndex], child, getDepthOfSearch(), getIntensityOfMutation());
			for (int i = 0; i < this.aoMemoryOfSolutions.length; i++) {
				if (child.getObjectiveFunctionValue() > this.aoMemoryOfSolutions[i].getObjectiveFunctionValue()) {
					this.aoMemoryOfSolutions[i] = child;
					break;
				}
			}
			candidateObjectiveVal=child.getObjectiveFunctionValue();

			break;
		default:
			System.out.println("you must enter a number between 0-6");
			candidateObjectiveVal = -1;
			break;
		}

		// take into account crossover
		// update best solution
		int bestindex=0;
		for (int i = 0; i < this.aoMemoryOfSolutions.length; i++) {
			if (this.oBestSolution.getObjectiveFunctionValue() > this.aoMemoryOfSolutions[i]
					.getObjectiveFunctionValue()) {
				//this.oBestSolution = this.aoMemoryOfSolutions[i];
				bestindex=i;
			}
		}
		updateBestSolution(bestindex);
		return candidateObjectiveVal;

	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		// TODO - apply heuristic and return the objective value of the candidate
		// solution
		// remembering to keep track/update the best solution

		return 7;

	}

	@Override
	public String bestSolutionToString() {
		// TODO
		return getBestSolution().toString();
	}

	@Override
	public boolean compareSolutions(int iIndexA, int iIndexB) {
		// TODO return true if the objective values of the two solutions are the same,
		// else false

		int[] value1 = this.aoMemoryOfSolutions[iIndexA].getSolutionRepresentation().getSolutionRepresentation();
		int[] value2 = this.aoMemoryOfSolutions[iIndexB].getSolutionRepresentation().getSolutionRepresentation();

		int numberOfLocations = this.oInstance.getNumberOfLocations();

		for (int i = 0; i < numberOfLocations; i++) {
			if (value1[i] != value2[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void copySolution(int iIndexA, int iIndexB) {
		// TODO - BEWARE this should copy the solution, not the reference to it!
		// That is, that if we apply a heuristic to the solution in index 'b',
		// then it does not modify the solution in index 'a' or vice-versa.

		this.aoMemoryOfSolutions[iIndexB] = this.aoMemoryOfSolutions[iIndexA].clone();

	}

	@Override
	public double getBestSolutionValue() {
		// TODO
		return oBestSolution.getObjectiveFunctionValue();
	}

	@Override
	public double getFunctionValue(int index) {
		// TODO
		return aoMemoryOfSolutions[index].getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		// TODO return an array of heuristic IDs based on the heuristic's type.
		if (type == HeuristicType.LOCAL_SEARCH) {
			return new int[] { 3, 4 };
		} else if (type == HeuristicType.MUTATION) {
			return new int[] { 0, 1, 2 };
		} else if (type == HeuristicType.CROSSOVER) {
			return new int[] { 5, 6 };
		} else {
			return null;
		}

	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		// TODO return the array of heuristic IDs that use depth of search.
		return new int[] { 3, 4 };
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		// TODO return the array of heuristic IDs that use intensity of mutation.
		return new int[] { 0, 1, 2, 5, 6 };
	}

	@Override
	public int getNumberOfHeuristics() {
		// TODO - has to be hard-coded due to the design of the HyFlex framework...
		return 7;
	}

	@Override
	public int getNumberOfInstances() {
		// TODO return the number of available instances
		return instanceFiles.length;
	}

	@Override
	public void initialiseSolution(int index) {
		// TODO - initialise a solution in index 'index'
		// making sure that you also update the best solution!
		aoMemoryOfSolutions[index] = this.oInstance.createSolution(InitialisationMode.RANDOM);
		/*aoMemoryOfSolutions[index].setObjectiveFunctionValue(this.oObjectiveFunction
				.getObjectiveFunctionValue(aoMemoryOfSolutions[index].getSolutionRepresentation()));
		aoMemoryOfSolutions[index].getObjectiveFunctionValue();*/

		// update best
		int bestindex=0;
		if (oBestSolution != null) {
			for (int i = 0; i < this.aoMemoryOfSolutions.length; i++) {
				if (this.aoMemoryOfSolutions[i] != null && this.oBestSolution
						.getObjectiveFunctionValue() > this.aoMemoryOfSolutions[i].getObjectiveFunctionValue()) {
					//this.oBestSolution = this.aoMemoryOfSolutions[i];
					bestindex=i;
				}
			}
		} else {
			//this.oBestSolution = aoMemoryOfSolutions[index];
		}

		updateBestSolution(bestindex);

	}

	// TODO implement the instance reader that this method uses
	// to correctly read in the PWP instance, and set up the objective function.
	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "pwp" + SEP + instanceFiles[instanceId] + ".pwp";

		String absolutePath = Paths.get("").toAbsolutePath().toString();
		Path path = Paths.get(absolutePath + "\\" + instanceName);

		Random random = new Random(seed);

		PWPInstanceReader oPwpReader = new PWPInstanceReader();
		this.oInstance = oPwpReader.readPWPInstance(path, random);

		oObjectiveFunction = oInstance.getPWPObjectiveFunction();

		for (HeuristicInterface h : aoHeuristics) {
			h.setObjectiveFunction(oObjectiveFunction);
		}

	}

	@Override
	public void setMemorySize(int size) {

		// TODO sets a new memory size
		// IF the memory size is INCREASED, then
		// the existing solutions should be copied to the new memory at the same
		// indices.
		// IF the memory size is DECREASED, then
		// the first 'size' solutions are copied to the new memory.

		PWPSolutionInterface[] temp = new PWPSolutionInterface[size];

		if (aoMemoryOfSolutions != null) {
			if (aoMemoryOfSolutions.length < size) {
				// if increased
				for (int i = 0; i < aoMemoryOfSolutions.length; i++) {
					temp[i] = aoMemoryOfSolutions[i]; // same indices
				}
			} else {
				for (int i = 0; i < size; i++) {
					temp[i] = aoMemoryOfSolutions[i];
				}
			}

		}
		aoMemoryOfSolutions = temp;
	}

	@Override
	public String solutionToString(int index) {
		// TODO
		return this.aoMemoryOfSolutions[index].toString();
	}

	@Override
	public String toString() {
		return "psybb4's G52AIM PWP";
	}

	private void updateBestSolution(int index) {
		// TODO
		
		oBestSolution = aoMemoryOfSolutions[index].clone();
	}

	@Override
	public PWPInstanceInterface getLoadedInstance() {
		return this.oInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {
		int[] city_ids = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		Location[] route = Arrays.stream(city_ids).boxed().map(getLoadedInstance()::getLocationForDelivery)
				.toArray(Location[]::new);
		return route;
	}

	public static void main(String[] args) {
		long seed = 564321;
		// create AIM_PWP object
		AIM_PWP aimPwp = new AIM_PWP(seed);
		// how many solutions like parents
		aimPwp.setMemorySize(2);
		// load locations from file
		aimPwp.loadInstance(2);
		
		aimPwp.setDepthOfSearch(0.6);
		aimPwp.setIntensityOfMutation(0.6);
		
		// set solutions on solution memory
		for (int i = 0; i < aimPwp.aoMemoryOfSolutions.length; i++) {
			aimPwp.initialiseSolution(i);
		}
		//aimPwp.applyHeuristic(5, 0, 1);
		// run loaded solution
		PWPView view= new PWPView(aimPwp.oInstance,aimPwp,Color.yellow,Color.red);
		
		int rnd=0;
		for (int i = 0; i < 1000000; i++) {
			aimPwp.setDepthOfSearch(aimPwp.rng.nextDouble());
			aimPwp.setIntensityOfMutation(aimPwp.rng.nextDouble());
			rnd= aimPwp.rng.nextInt(aimPwp.getNumberOfHeuristics());
			System.out.println(i+". try heu= "+rnd);
			aimPwp.applyHeuristic(rnd, 0, 1);
			
			int m=0;
		}
		//aimPwp.applyHeuristic(4, 0, 1);
		//run evulated solution
		PWPView view2= new PWPView(aimPwp.oInstance,aimPwp,Color.yellow,Color.red);
		
		
		// solution interface only keeps delivery locations 
	

		

	}

}
