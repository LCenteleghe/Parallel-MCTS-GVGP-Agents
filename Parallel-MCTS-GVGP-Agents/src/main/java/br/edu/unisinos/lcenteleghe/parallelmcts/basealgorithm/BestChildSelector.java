package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

public interface BestChildSelector<S extends ObservableState<S, A>, A extends Enum<A>> {
	Node<S, A> selectBest(Node<S, A> node);
}
