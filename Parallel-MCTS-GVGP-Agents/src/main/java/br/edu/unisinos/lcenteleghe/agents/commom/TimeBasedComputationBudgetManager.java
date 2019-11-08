package br.edu.unisinos.lcenteleghe.agents.commom;
//package tcclab.lcenteleghe.commom;
//
//import tcclab.mcts.algorithm.ComputationBudgetManager;
//import tcclab.mcts.algorithm.IterationListener;
//import tools.ElapsedCpuTimer;
//
//public class TimeBasedComputationBudgetManager implements ComputationBudgetManager, IterationListener {
//
//	private static final int REMAINING_TIME_LIMIT = 5;
//
//	private double avgTimeTaken;
//	private double acumTimeTaken;
//	private int numIters;
//
//	private ElapsedCpuTimer elapsedTimerIteration = new ElapsedCpuTimer();
//	
//	private ElapsedCpuTimer remainingTimeTimer;
//
//	private TimeBasedComputationBudgetManager(ElapsedCpuTimer remainingTimeTimer) {
//		this.remainingTimeTimer = remainingTimeTimer;
//	}
//
//	public static TimeBasedComputationBudgetManager newInstance(ElapsedCpuTimer remainingTimeTimer) {
//		return new TimeBasedComputationBudgetManager(remainingTimeTimer);
//	}
//	
//	public void updateRemainingTime(ElapsedCpuTimer remainingTimeTimer) {
//		this.remainingTimeTimer = remainingTimeTimer;
//	}
//
//	public void informIterationStarted() {
//		elapsedTimerIteration = new ElapsedCpuTimer();
//	}
//
//	public void informIterationEnded() {
//		this.numIters++;
//		this.acumTimeTaken += elapsedTimerIteration.elapsedMillis();
//		this.avgTimeTaken = acumTimeTaken / numIters;
//	}
//
//	@Override
//	public boolean shouldComputationContinue() {
//		return remainingTimeTimer.remainingTimeMillis() > 2 * avgTimeTaken && remainingTimeTimer.remainingTimeMillis() > REMAINING_TIME_LIMIT;
//	}
//
//}
