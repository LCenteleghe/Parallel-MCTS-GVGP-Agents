package br.edu.unisinos.lcenteleghe.parallelmcts.treemerger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;

public class BestTreeMerger<S extends ObservableState<S, A>, A extends Enum<A>> implements TreeMerger<S, A> {

	private BestTreeMerger() {
		super();
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> BestTreeMerger<S, A> getInstance() {
		return new BestTreeMerger<>();
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
			int maximumNumberOfVisitsBetweenNodes = childrenWithSameSourceActionEntry.getValue()
					.stream()
					.mapToInt(Node::getNumberVisits)
					.max()
					.getAsInt();

			double maximumRewardOfVisitsBetweenNodes = childrenWithSameSourceActionEntry.getValue()
					.stream()
					.mapToDouble(Node::getTotalValue)
					.max()
					.getAsDouble();

			A sourceAction = childrenWithSameSourceActionEntry.getKey();

			newTreeRoot.addNewChildNode(sourceAction,
					maximumNumberOfVisitsBetweenNodes, maximumRewardOfVisitsBetweenNodes);
		}

		return newTreeRoot;
	}

}
