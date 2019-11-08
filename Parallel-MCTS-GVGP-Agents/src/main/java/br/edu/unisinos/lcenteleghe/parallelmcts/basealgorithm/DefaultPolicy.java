package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

public interface DefaultPolicy<S extends ObservableState<S, A>, A extends Enum<A>> {
	double apply(ObservableState<S, A> state);
}
