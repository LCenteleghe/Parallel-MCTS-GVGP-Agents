package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

public interface MutableState<T, A extends Enum<A>> extends ObservableState<T, A> {
	void advance(A action);
	MutableState<T, A> advanceAndGet(A action);
}
