package br.edu.unisinos.lcenteleghe.parallelmcts.treemerger;

import java.util.List;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;

public interface TreeMerger<S extends ObservableState<S, A>, A extends Enum<A>> {
	Node<S, A> merge(List<Node<S, A>> treesRoots);
}
