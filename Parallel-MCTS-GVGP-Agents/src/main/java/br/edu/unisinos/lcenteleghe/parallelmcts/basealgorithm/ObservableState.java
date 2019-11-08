package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

import java.util.List;

public interface ObservableState<T, A extends Enum<A>> {
	boolean isFinal();
	T getObservation();
	List<A> getAvailableActions();
	MutableState<T, A> copyAsMutable();
}
