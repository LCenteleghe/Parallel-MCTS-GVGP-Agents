
package br.edu.unisinos.lcenteleghe.parallelmcts.parallelization;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AtomicDouble;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.DefaultPolicy;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;

public class InterruptibleDefaultPolicyWithLeafParallelization<S extends ObservableState<S, A>, A extends Enum<A>>
		implements DefaultPolicy<S, A> {
	static final Logger LOG = LoggerFactory.getLogger(InterruptibleDefaultPolicyWithLeafParallelization.class);

	private DefaultPolicy<S, A> baseDefaultPolicy;

	private int parallelismLevel;

	private InterruptibleDefaultPolicyWithLeafParallelization(Builder<S, A> builder) {
		this.baseDefaultPolicy = builder.baseDefaultPolicy;
		this.parallelismLevel = builder.parallelismLevel;
	}

	@Override
	public double apply(ObservableState<S, A> observableState) {
		ExecutorService executorService = ExecutorServiceFactory.getExecutorService();

		AtomicInteger totalSimulations = new AtomicInteger();
		AtomicDouble totalReward = new AtomicDouble();

		List<Future<?>> submitedTasks = new ArrayList<>(parallelismLevel);
		
		CountDownLatch countdownLatch = new CountDownLatch(parallelismLevel);

		try {
			for (int i = 0; i < parallelismLevel; i++) {
				submitedTasks.add(executorService.submit(() -> {
					totalReward.addAndGet(baseDefaultPolicy.apply(observableState));
					totalSimulations.incrementAndGet();
					countdownLatch.countDown();
				}));
			}

			countdownLatch.await();
		} catch (InterruptedException e) {
			LOG.trace("Inturrupted");
			Thread.currentThread().interrupt();
			ConcurrencyUtils.cancelAll(submitedTasks);
			try {
				countdownLatch.await();
			} catch (InterruptedException e1) {
				throw new RuntimeException("Double interruption", e1);
			}
		}
		
		if(totalSimulations.get() == 0) {
			throw new IllegalStateException("No simulations run");
		}

		return totalReward.doubleValue() / totalSimulations.get();
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> Builder<S, A> builder() {
		return new Builder<>();
	}

	public static final class Builder<S extends ObservableState<S, A>, A extends Enum<A>> {
		private DefaultPolicy<S, A> baseDefaultPolicy;
		private int parallelismLevel = 1;

		private Builder() {
		}

		public Builder<S, A> baseDefaultPolicy(DefaultPolicy<S, A> baseDefaultPolicy) {
			this.baseDefaultPolicy = baseDefaultPolicy;
			return this;
		}

		public Builder<S, A> parallelismLevel(int parallelismLevel) {
			this.parallelismLevel = parallelismLevel;
			return this;
		}

		public InterruptibleDefaultPolicyWithLeafParallelization<S, A> build() {
			return new InterruptibleDefaultPolicyWithLeafParallelization<S, A>(this);
		}
	}

}
