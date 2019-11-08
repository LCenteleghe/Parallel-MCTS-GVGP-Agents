package br.edu.unisinos.lcenteleghe.agents.commom;

import br.edu.unisinos.lcenteleghe.parallelmcts.uct.StateObservationEvaluator;
import ontology.Types;

public class CustomStateObservationEvaluator implements StateObservationEvaluator<GameState> {
	private static final double HUGE_NEGATIVE = -10000000.0;
	private static final double HUGE_POSITIVE = 10000000.0;

	private CustomStateObservationEvaluator() {
	}

	public static CustomStateObservationEvaluator newInstance() {
		return new CustomStateObservationEvaluator();
	}

	@Override
	public double evaluate(GameState gameState) {
		boolean gameOver = gameState.isGameOver();
		Types.WINNER win = gameState.getGameWinner();
		double rawScore = gameState.getGameScore();

		if (gameOver && win == Types.WINNER.PLAYER_LOSES)
			rawScore += HUGE_NEGATIVE;

		if (gameOver && win == Types.WINNER.PLAYER_WINS)
			rawScore += HUGE_POSITIVE;
		
		return rawScore;
	}

}
