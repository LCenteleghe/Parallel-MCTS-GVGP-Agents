package br.edu.unisinos.lcenteleghe.parallelmcts.treemerger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;

public class SumTreeMerger<S extends ObservableState<S, A>, A extends Enum<A>> implements TreeMerger<S, A> {

	private SumTreeMerger() {
		super();
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> SumTreeMerger<S, A> getInstance() {
		return new SumTreeMerger<>();
	}

	@Override
	public Node<S, A> merge(List<Node<S, A>> treesRoots) {
		if (treesRoots.isEmpty()) {
			throw new IllegalArgumentException("Cannot merge empty trees's roots list.");
		}

		if (treesRoots.size() == 1) {
			return treesRoots.get(0);
		}

		Node<S, A> newTreeRoot = Node.newRootNode();

		int globalNumberOfSimulations = treesRoots.stream().mapToInt(Node::getNumberVisits).sum();

		Map<A, List<Node<S, A>>> childrenPerSourceAction = treesRoots.stream()
				.flatMap(n -> n.getChildren().stream())
				.collect(Collectors.groupingBy(Node::getSourceAction));

		for (Entry<A, List<Node<S, A>>> childrenWithSameSourceActionEntry : childrenPerSourceAction.entrySet()) {
			double weightedNumberOfVisitsSum = childrenWithSameSourceActionEntry.getValue()
					.stream()
					.mapToDouble(n -> calculateWeightedNumberOfVisits(n, globalNumberOfSimulations))
					.sum();

			double weightedTotalRewardSum = childrenWithSameSourceActionEntry.getValue()
					.stream()
					.mapToDouble(n -> calculateWeightedReward(n, globalNumberOfSimulations))
					.sum();

			A sourceAction = childrenWithSameSourceActionEntry.getKey();

			newTreeRoot.addNewChildNode(sourceAction,
					(int) weightedNumberOfVisitsSum, weightedTotalRewardSum);
		}

		return newTreeRoot;
	}

	private double calculateWeightedNumberOfVisits(Node<S, A> node, int globalNumberOfSimulations) {
		return node.getNumberVisits() * calculateNodeWeight(node, globalNumberOfSimulations);
	}

	private Double calculateWeightedReward(Node<S, A> node, int globalNumberOfSimulations) {
		return node.getTotalValue() * calculateNodeWeight(node, globalNumberOfSimulations);
	}

	private double calculateNodeWeight(Node<S, A> node, int globalNumberOfSimulations) {
		return node.getParent().getNumberVisits() / (double) globalNumberOfSimulations;
	}
}
