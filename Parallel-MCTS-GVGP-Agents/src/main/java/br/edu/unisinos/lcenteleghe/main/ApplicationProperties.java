package br.edu.unisinos.lcenteleghe.main;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.edu.unisinos.lcenteleghe.parallelmcts.treemerger.TreeMergingMethod;

public class ApplicationProperties {
	private static ApplicationProperties instance;

	private List<Integer> levels;

	private List<Integer> agentsIdxs;

	private List<Integer> numThreads;
	
	private Integer repeats;
	
	private TreeMergingMethod treeMergingMethod;
	
	private double sigmaUct;

	public ApplicationProperties() {
		String numLevelsStr = System.getProperties().getProperty(SystemProperties.NUM_LEVELS.name().toLowerCase(),
				"0"); //0,1,2,3
		levels = Stream.of(numLevelsStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());

		String agentsIdxsStr = System.getProperties().getProperty(SystemProperties.AGENT_IDX.name().toLowerCase(),
				"0,1,2,3");
		agentsIdxs = Stream.of(agentsIdxsStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());

		String numThreadsStr = System.getProperties().getProperty(SystemProperties.NUM_THREADS.name().toLowerCase(),
				"2,4,8,16,32");
		numThreads = Stream.of(numThreadsStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());
				
		repeats = Integer.valueOf(System.getProperties().getProperty(SystemProperties.REPEATS.name().toLowerCase(),
				"1"));
		
		treeMergingMethod = TreeMergingMethod.valueOf(System.getProperties().getProperty(SystemProperties.MERGE_METHOD.name().toLowerCase(),
				"SUM"));
		
		
		sigmaUct = Double.valueOf(System.getProperties().getProperty(SystemProperties.SIGMA_UCT.name().toLowerCase(),
				"1.4142135623730951")); //sqrt(2)

		System.out.print("Application Properties: ");
		System.out.print("Threads: " + numThreads);
		System.out.print(" | Levels: " + levels);
		System.out.print(" | Agents: " + agentsIdxs);
		System.out.print(" | Reps: " + repeats);
		System.out.print(" | MergeMethod: " + treeMergingMethod.name());
		System.out.println(" | SigmaUct: " + sigmaUct);
	}

	public List<Integer> getLevels() {
		return levels;
	}

	public List<Integer> getAgentsIdxs() {
		return agentsIdxs;
	}

	public List<Integer> getNumThreads() {
		return numThreads;
	}
	
	public Integer getRepeats() {
		return repeats;
	}

	public TreeMergingMethod getTreeMergingMethod() {
		return treeMergingMethod;
	}

	public double getSigmaUct() {
		return sigmaUct;
	}

	public static ApplicationProperties getInstance() {
		if (instance == null) {
			instance = new ApplicationProperties();
		}
		return instance;
	}

	public enum SystemProperties {
		AGENT_IDX,
		NUM_LEVELS,
		NUM_THREADS,
		REPEATS,
		MERGE_METHOD,
		SIGMA_UCT
	}
}
