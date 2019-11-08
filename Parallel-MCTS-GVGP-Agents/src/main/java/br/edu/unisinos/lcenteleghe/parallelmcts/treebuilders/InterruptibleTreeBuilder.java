package br.edu.unisinos.lcenteleghe.parallelmcts.treebuilders;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;

public interface InterruptibleTreeBuilder<S extends ObservableState<S, A>, A extends Enum<A>> extends TreeBuilder<S, A> {

}
