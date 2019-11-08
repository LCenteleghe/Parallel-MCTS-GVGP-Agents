package br.edu.unisinos.lcenteleghe.agents.commom;

import java.util.List;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.MutableState;
import core.game.StateObservation;
import ontology.Types;
import ontology.Types.ACTIONS;
import ontology.Types.WINNER;

public class GameState implements MutableState<GameState, Types.ACTIONS>{

	private StateObservation stateObservation;
	
	private GameState(StateObservation stateObservation) {
		this.stateObservation = stateObservation;
	}

	public static GameState newInstance(StateObservation stateObservation) {
		return new GameState(stateObservation);
	}
	
	@Override
	public boolean isFinal() {
		return stateObservation.isGameOver();
	}

	@Override
	public GameState getObservation() {
		return this;
	}

	@Override
	public List<ACTIONS> getAvailableActions() {
		return stateObservation.getAvailableActions();
	}

	@Override
	public MutableState<GameState, ACTIONS> copyAsMutable() {
		return newInstance(stateObservation.copy());
	}

	@Override
	public void advance(ACTIONS action) {
		stateObservation.advance(action);
	}

	@Override
	public MutableState<GameState, ACTIONS> advanceAndGet(ACTIONS action) {
		advance(action);
		return this;
	}

	public boolean isGameOver() {
		return stateObservation.isGameOver();
	}

	public WINNER getGameWinner() {
		return stateObservation.getGameWinner();
	}

	public double getGameScore() {
		return stateObservation.getGameScore();
	}
}
