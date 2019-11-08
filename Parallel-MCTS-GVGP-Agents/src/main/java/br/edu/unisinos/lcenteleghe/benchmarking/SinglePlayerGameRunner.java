package br.edu.unisinos.lcenteleghe.benchmarking;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.player.AbstractPlayer;
import tools.Utils;
import tracks.ArcadeMachine;

public class SinglePlayerGameRunner {
	private static final int NUMBER_OF_LEVELS = 1;

	private static SinglePlayerGameRunner instance = new SinglePlayerGameRunner();

	private String spGamesCollection;
	private String[][] gamesMetadata;
	private int seed;

	private SinglePlayerGameRunner() {
		this.spGamesCollection = "examples/all_games_sp.csv";
		this.gamesMetadata = Utils.readGames(spGamesCollection);
		this.seed = new Random().nextInt();
	}

	public static SinglePlayerGameRunner getInstance() {
		return instance;
	}

	public RunResults run(Game game, int level) {
		double[] results = ArcadeMachine.playOneGame(getGameFileName(game), getGameLevelFileName(game, level), null,
				seed);
		return RunResults.newInstance(tracks.singlePlayer.tools.human.Agent.class, game, level, results);
	}

	public RunResults run(Game game) {
		return run(game, 0);
	}

	public RunResults run(Class<? extends AbstractPlayer> agent, Game game, int gameLevel) {
		return run(agent, game, gameLevel, false);
	}

	public RunResults runWithVisuals(Class<? extends AbstractPlayer> agent, Game game, int gameLevel) {
		return run(agent, game, gameLevel, true);
	}

	public RunResults runWithVisuals(Class<? extends AbstractPlayer> agent, Game game) {
		return runWithVisuals(agent, game, 0);
	}

	private RunResults run(Class<? extends AbstractPlayer> agent, Game game, int level, boolean visuals) {
		double[] results = ArcadeMachine.runOneGame(getGameFileName(game), getGameLevelFileName(game, level), visuals,
				agent.getCanonicalName(), null, seed, 0);
		return RunResults.newInstance(agent, game, level, results);
	}

	public List<RunResults> runSameGameManyTimes(Class<? extends AbstractPlayer> agent, Game game, int gameLevel,
			int numberOfTimes) {
		List<RunResults> results = new LinkedList<>();
		for (int i = 0; i < numberOfTimes; i++) {
			results.add(run(agent, game, gameLevel));
		}
		return results;
	}

	public List<RunResults> runAllGames(Class<? extends AbstractPlayer> agent) {
		return runAllGames(agent, 1);
	}

	public List<RunResults> runAllGames(Class<? extends AbstractPlayer> agent, int timesPerLevel) {
		List<RunResults> results = new LinkedList<>();
		for (Game game : Game.values()) {
			for (int i = 0; i < NUMBER_OF_LEVELS; i++) {
				results.addAll(runSameGameManyTimes(agent, game, i, timesPerLevel));
			}
		}
		return results;
	}

	private String getGameFileName(Game game) {
		return gamesMetadata[game.ordinal()][0];
	}

	private String getGameName(Game game) {
		return gamesMetadata[game.ordinal()][1];
	}

	private String getGameLevelFileName(Game game, int levelIdx) {
		String gameName = getGameName(game);
		return getGameFileName(game).replace(gameName, gameName + "_lvl" + levelIdx);
	}
}
