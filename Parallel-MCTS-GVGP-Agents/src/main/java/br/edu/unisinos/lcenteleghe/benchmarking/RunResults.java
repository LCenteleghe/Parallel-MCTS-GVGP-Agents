package br.edu.unisinos.lcenteleghe.benchmarking;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.edu.unisinos.lcenteleghe.util.PackageNameUtils;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;

public class RunResults {
	private static final int COLUMNS_WIDTH = 20;
	private String agent;
	private String game;
	private int level;
	private double status;
	private double score;
	private int timeSteps;
	private long simsPerSec;

	public RunResults(String agent, String game, int level, double status, double score, int timeSteps) {
		super();
		this.agent = agent;
		this.game = game;
		this.level = level;
		this.status = status;
		this.score = score;
		this.timeSteps = timeSteps;
	}

	public static RunResults newInstance(Class<?> agent, Game game, int level, double[] resultTriplet) {
		return new RunResults(
				PackageNameUtils.extractPackageLastName(agent),
				game.name(),
				level,
				resultTriplet[0],
				resultTriplet[1],
				(int) resultTriplet[2]);
	}


	public void prettyPrintResults() {
		AsciiTable asciiTable = new AsciiTable();

		asciiTable.getRenderer().setCWC(new CWC_LongestWordMin(COLUMNS_WIDTH));

		asciiTable.addRow(getResultsAsString());
		System.out.println(asciiTable.render());
	}

	public static void printDefaultHeader() {
		AsciiTable asciiTable = new AsciiTable();
		asciiTable.getRenderer().setCWC(new CWC_LongestWordMin(COLUMNS_WIDTH));
		asciiTable.addRow(getDefaultHeader());
		System.out.println(asciiTable.render());
	}

	public List<String> getResultsAsString() {
		return Stream.of(getGame(), getLevel(), getAgent(), getStatus(), getScore(), getTimeSteps(), getSimsPerSec())
				.map(String::valueOf).collect(Collectors.toList());
	}

	public static List<String> getDefaultHeader() {
		return Arrays.asList("Game", "Level", "Agent", "Status", "Score", "Timesteps", "SimPerSec");
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getStatus() {
		return status;
	}

	public void setStatus(double status) {
		this.status = status;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getTimeSteps() {
		return timeSteps;
	}

	public void setTimeSteps(int timeSteps) {
		this.timeSteps = timeSteps;
	}

	public long getSimsPerSec() {
		return simsPerSec;
	}

	public void setSimsPerSec(long simsPerSec) {
		this.simsPerSec = simsPerSec;
	}
}
