package com.g52aim.project.tsp.runners;

import java.util.Arrays;

import com.g52aim.project.tsp.G52AIMTSP;
import com.g52aim.project.tsp.hyperheuristics.PSYCY_HH;
import com.g52aim.project.tsp.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import FlowShop.FlowShop;
import SAT.SAT;
import travelingSalesmanProblem.TSP;

public class SR_IE_Runner {

	public static void main(String [] args) {

		long seed = 15032018l;
		long timeLimit = 60_000l;

		double result[] = new double[5];

		for(int i=0; i < result.length; i++) {
			//ProblemDomain problem = new TSP(seed-i);
			ProblemDomain problem = new G52AIMTSP(seed);
			problem.loadInstance(10);
			HyperHeuristic hh = new PSYCY_HH(seed);
			hh.setTimeLimit(timeLimit);
			hh.loadProblemDomain(problem);
			hh.run();

			result[i] = hh.getBestSolutionValue();
			System.out.println("f(s_best) = " + hh.getBestSolutionValue());

		}
		System.out.println(Arrays.toString(result));

		System.out.println("-----------------------------------------------");

	}

}
