package br.edu.unisinos.lcenteleghe.agents.sinc_mcts;

import br.edu.unisinos.lcenteleghe.agents.commom.CustomBestChildSelector;
import br.edu.unisinos.lcenteleghe.agents.commom.CustomStateObservationEvaluator;
import br.edu.unisinos.lcenteleghe.agents.commom.GameState;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ParallelizableUctMctsAlgorithm;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.RandomGeneratorFactory;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer {

	private ParallelizableUctMctsAlgorithm<GameState, Types.ACTIONS> mctsAlgorithm;

	/**
	 * Public constructor with state observation and time due.
	 * 
	 * @param so
	 *            state observation of the current game.
	 * @param elapsedTimer
	 *            Timer for the controller creation.
	 */
	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		this.mctsAlgorithm = ParallelizableUctMctsAlgorithm.<GameState, Types.ACTIONS>builder()
				.bestChildSelector(CustomBestChildSelector.newInstance(RandomGeneratorFactory.getRandomGenerator()))
				.stateObservationEvaluator(CustomStateObservationEvaluator.newInstance())
				.maxTreeBuildingTime(40)
				.build();
	}

	/**
	 * Picks an action. This function is called every game step to request an action
	 * from the player.
	 * 
	 * @param stateObs
	 *            Observation of the current state.
	 * @param elapsedTimer
	 *            Timer when the action returned is due.
	 * @return An action for the current state
	 */
	public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		return mctsAlgorithm.search(GameState.newInstance(stateObs));
	}
}
