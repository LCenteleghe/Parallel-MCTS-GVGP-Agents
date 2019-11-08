package br.edu.unisinos.lcenteleghe.parallelmcts.treemerger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;

public class RawTreeMerger<S extends ObservableState<S, A>, A extends Enum<A>> implements TreeMerger<S, A> {

	private RawTreeMerger() {
		super();
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> RawTreeMerger<S, A> getInstance() {
		return new RawTreeMerger<>();
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

		Map<A, List<Node<S, A>>> childrenPerSourceAction = treesRoots.stream()
				.flatMap(n -> n.getChildren().stream())
				.collect(Collectors.groupingBy(Node::getSourceAction));

		for (Entry<A, List<Node<S, A>>> childrenWithSameSourceActionEntry : childrenPerSourceAction.entrySet()) {
			int numberOfVisitsSum = childrenWithSameSourceActionEntry.getValue()
					.stream()
					.mapToInt(Node::getNumberVisits)
					.sum();

			double totalRewardSum = childrenWithSameSourceActionEntry.getValue()
					.stream()
					.mapToDouble(Node::getTotalValue)
					.sum();

			A sourceAction = childrenWithSameSourceActionEntry.getKey();

			newTreeRoot.addNewChildNode(sourceAction,
					numberOfVisitsSum/treesRoots.size(), totalRewardSum/treesRoots.size());
		}

		return newTreeRoot;
	}

}
