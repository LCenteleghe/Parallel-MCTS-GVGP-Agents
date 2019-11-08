package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

public interface TreePolicy<S extends ObservableState<S, A>, A extends Enum<A>> {
	Node<S, A> apply(Node<S, A> node);
}
