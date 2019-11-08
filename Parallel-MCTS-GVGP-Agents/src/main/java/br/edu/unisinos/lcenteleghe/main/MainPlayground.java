package br.edu.unisinos.lcenteleghe.main;

import java.util.Arrays;
import java.util.List;

import br.edu.unisinos.lcenteleghe.agents.commom.GlobalNumThreadsParamForAgent;
import br.edu.unisinos.lcenteleghe.benchmarking.Game;
import br.edu.unisinos.lcenteleghe.benchmarking.RunResults;
import br.edu.unisinos.lcenteleghe.benchmarking.SinglePlayerGameRunner;
import br.edu.unisinos.lcenteleghe.parallelmcts.parallelization.ExecutorServiceFactory;
import br.edu.unisinos.lcenteleghe.util.SimulationsPerSecondTracker;
import core.player.AbstractPlayer;

public class MainPlayground {

	public static void main(String[] args) throws InterruptedException {
		ExecutorServiceFactory.start();
		GlobalNumThreadsParamForAgent.numThreads = 1;

		List<Class<? extends AbstractPlayer>> agents = Arrays.asList(
				br.edu.unisinos.lcenteleghe.agents.sinc_mcts.Agent.class,
				br.edu.unisinos.lcenteleghe.agents.leaf_parallel_mcts.Agent.class,
				br.edu.unisinos.lcenteleghe.agents.root_parallel_mcts.Agent.class,
				br.edu.unisinos.lcenteleghe.agents.tree_parallel_mcts.Agent.class);

		RunResults.printDefaultHeader();

		/* All games */
//		for (Game game : Game.values())
//			for (Class<? extends AbstractPlayer> agent : agents) {
//				simPerSecTracker.startTracking();
//				RunResults runResults = SinglePlayerGameRunner.getInstance().run(agent, game, 0);
//				runResults.setSimsPerSec(simPerSecTracker.stopAndGet());
//				runResults.prettyPrintResults();
//				;
//			}

		/* Stop before start */
		// System.out.println("Press \"ENTER\" to continue...");
		// new Scanner(System.in).nextLine();

		/* 10x Alien */
//		for (int i = 0; i < 10; i++) {
//			RunResults runResults = SinglePlayerGameRunner.getInstance().run(
//					agents.get(3),
//					Game.ALIENS, 0);
//			runResults.prettyPrintResults();
//		}

		/* With visual */
		 RunResults runResults = SinglePlayerGameRunner.getInstance().runWithVisuals(
		 agents.get(0),
		 Game.ALIENS, 0);
	//	 runResults.prettyPrint();
	}
}
