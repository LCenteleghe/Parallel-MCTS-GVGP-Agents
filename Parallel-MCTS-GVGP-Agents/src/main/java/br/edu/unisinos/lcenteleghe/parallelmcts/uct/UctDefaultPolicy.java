
package br.edu.unisinos.lcenteleghe.parallelmcts.uct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.DefaultPolicy;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.MutableState;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.RandomGeneratorFactory;
import br.edu.unisinos.lcenteleghe.util.ListUtils;
import br.edu.unisinos.lcenteleghe.util.SimulationsPerSecondTracker;

public class UctDefaultPolicy<S extends ObservableState<S, A>, A extends Enum<A>> implements DefaultPolicy<S, A> {
	private static final Logger LOG = LoggerFactory.getLogger(UctDefaultPolicy.class);

	private int maxNumberOfStepsAhead;

	private StateObservationEvaluator<S> stateEvaluator;

	private UctDefaultPolicy(Builder<S, A> builder) {
		this.maxNumberOfStepsAhead = builder.maxNumberOfStepsAhead;
		this.stateEvaluator = builder.stateEvaluator;
	}
	
	@Override
	public double apply(ObservableState<S, A> observableState) {
		MutableState<S, A> mutableState = observableState.copyAsMutable();

		int numberOfSteps = 0;
		while (!mutableState.isFinal() && numberOfSteps < maxNumberOfStepsAhead && !Thread.currentThread().isInterrupted()) {
			mutableState.advance(ListUtils.selectRandomValue(mutableState.getAvailableActions(), RandomGeneratorFactory.getRandomGenerator()));
			numberOfSteps++;
		}
		
		SimulationsPerSecondTracker.getInstance().incrementSimCounter();
		
		return stateEvaluator.evaluate(mutableState.getObservation());
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> Builder<S, A> builder() {
		return new Builder<>();
	}

	public static final class Builder<S extends ObservableState<S, A>, A extends Enum<A>> {
		private int maxNumberOfStepsAhead;
		private StateObservationEvaluator<S> stateEvaluator;

		private Builder() {
		}

		public Builder<S, A> maxNumberOfStepsAhead(int maxNumberOfStepsAhead) {
			this.maxNumberOfStepsAhead = maxNumberOfStepsAhead;
			return this;
		}

		public Builder<S, A> stateEvaluator(StateObservationEvaluator<S> stateEvaluator) {
			this.stateEvaluator = stateEvaluator;
			return this;
		}

		public UctDefaultPolicy<S, A> build() {
			return new UctDefaultPolicy<S, A>(this);
		}
	}

	
}
