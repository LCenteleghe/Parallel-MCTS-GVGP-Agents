package br.edu.unisinos.lcenteleghe.main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.unisinos.lcenteleghe.agents.commom.GlobalNumThreadsParamForAgent;
import br.edu.unisinos.lcenteleghe.benchmarking.Game;
import br.edu.unisinos.lcenteleghe.benchmarking.RunParams;
import br.edu.unisinos.lcenteleghe.benchmarking.RunResults;
import br.edu.unisinos.lcenteleghe.benchmarking.SinglePlayerGameRunner;
import br.edu.unisinos.lcenteleghe.parallelmcts.parallelization.ExecutorServiceFactory;
import br.edu.unisinos.lcenteleghe.util.RunResultsFileWriter;
import br.edu.unisinos.lcenteleghe.util.SimulationsPerSecondTracker;
import core.player.AbstractPlayer;

public class MainFinal {
	private static final Logger LOG = LoggerFactory.getLogger(MainFinal.class);

	static List<Class<? extends AbstractPlayer>> agents = Arrays.asList(
			br.edu.unisinos.lcenteleghe.agents.sinc_mcts.Agent.class,
			br.edu.unisinos.lcenteleghe.agents.leaf_parallel_mcts.Agent.class,
			br.edu.unisinos.lcenteleghe.agents.root_parallel_mcts.Agent.class,
			br.edu.unisinos.lcenteleghe.agents.tree_parallel_mcts.Agent.class);

	public static void main(String[] args) {
		ExecutorServiceFactory.start();
		List<RunParams> runParamsLst = createRunParamsLst(ApplicationProperties.getInstance());

		for (RunParams runParams : runParamsLst) {
			try {
				run(runParams);
			} catch (Exception e) {
				LOG.warn("Run interrupated by exception. Run params: " + runParams, e);
			}
		}
	}

	private static void run(RunParams runParams) {
		SimulationsPerSecondTracker simPerSecTracker = SimulationsPerSecondTracker.getInstance();
		List<RunResults> runResultsLst = new LinkedList<>();
		GlobalNumThreadsParamForAgent.numThreads = runParams.getNumThreads();

		runParams.prettyPrint();
		RunResults.printDefaultHeader();
		for (Game game : Game.values()) {
			simPerSecTracker.startTracking();

			RunResults runResults = SinglePlayerGameRunner.getInstance()
					.run(runParams.getAgentUnderEvaluation(), game, runParams.getLevel());
			runResults.setSimsPerSec(simPerSecTracker.stopAndGet());
			runResults.prettyPrintResults();
			runResultsLst.add(runResults);
		}

		RunResultsFileWriter.write(runParams, runResultsLst);
	}

	private static List<RunParams> createRunParamsLst(ApplicationProperties appProps) {
		List<RunParams> runParamsLst = new LinkedList<>();

		for (int i = 0; i < appProps.getRepeats(); i++) {
			for (Integer level : ApplicationProperties.getInstance().getLevels()) {
				for (Integer agentIdx : appProps.getAgentsIdxs()) {
					Class<? extends AbstractPlayer> agentUnderEvaluation = agents.get(agentIdx);
					if (isSyncAgent(agentUnderEvaluation)) {
						runParamsLst.add(RunParams.builder()
								.agentUnderEvaluation(agentUnderEvaluation)
								.numThreads(1)
								.level(level)
								.rep(i)
								.build());
					} else {
						for (Integer numThreads : appProps.getNumThreads()) {
							runParamsLst.add(RunParams.builder()
									.agentUnderEvaluation(agentUnderEvaluation)
									.numThreads(numThreads)
									.level(level)
									.build());
						}
					}
				}
			}
		}
		return runParamsLst;
	}

	private static boolean isSyncAgent(Class<? extends AbstractPlayer> agentUnderEvaluation) {
		return agentUnderEvaluation.equals(br.edu.unisinos.lcenteleghe.agents.sinc_mcts.Agent.class);
	}
}
