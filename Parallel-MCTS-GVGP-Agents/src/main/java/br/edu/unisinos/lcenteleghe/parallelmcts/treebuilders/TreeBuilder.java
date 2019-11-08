package br.edu.unisinos.lcenteleghe.parallelmcts.treebuilders;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;

public interface TreeBuilder<S extends ObservableState<S, A>, A extends Enum<A>> {
	Node<S, A> buildTree(S initialState);
}
