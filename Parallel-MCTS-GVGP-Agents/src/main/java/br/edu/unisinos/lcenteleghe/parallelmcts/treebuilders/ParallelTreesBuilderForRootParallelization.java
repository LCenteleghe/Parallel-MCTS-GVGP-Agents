package br.edu.unisinos.lcenteleghe.parallelmcts.treebuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;
import br.edu.unisinos.lcenteleghe.parallelmcts.parallelization.ConcurrencyUtils;
import br.edu.unisinos.lcenteleghe.parallelmcts.parallelization.ExecutorServiceFactory;

class ParallelTreesBuilderForRootParallelization<S extends ObservableState<S, A>, A extends Enum<A>> {
	static final Logger LOG = LoggerFactory.getLogger(ParallelTreesBuilderForRootParallelization.class);

	private InterruptibleTreeBuilder<S, A> interruptibleTreeBuilder;
	private int numberOfParallelTrees;
	private long maxTreeBuildingTime;

	private ParallelTreesBuilderForRootParallelization(InterruptibleTreeBuilder<S, A> interruptibleTreeBuilder,
			int numberOfParallelTrees,
			long maxTreeBuildingTime) {
		this.interruptibleTreeBuilder = interruptibleTreeBuilder;
		this.numberOfParallelTrees = numberOfParallelTrees;
		this.maxTreeBuildingTime = maxTreeBuildingTime;
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> ParallelTreesBuilderForRootParallelization<S, A> newInstance(
			InterruptibleTreeBuilder<S, A> interruptibleTreeBuilder, int numberOfParallelTrees,
			long maxTreeBuildingTime) {
		return new ParallelTreesBuilderForRootParallelization<>(interruptibleTreeBuilder, numberOfParallelTrees,
				maxTreeBuildingTime);
	}

	public List<Node<S, A>> buildTrees(S initialState) {
		ExecutorService executorService = ExecutorServiceFactory.getExecutorService();

		List<Node<S, A>> builtTrees = Collections.<Node<S, A>>synchronizedList(new ArrayList<>());

		List<Future<?>> submitedTasks = new ArrayList<>(numberOfParallelTrees);

		CountDownLatch countdownLatch = new CountDownLatch(numberOfParallelTrees);
		
		for (int i = 0; i < numberOfParallelTrees; i++) {
			submitedTasks.add(executorService.submit(() -> {
				Node<S, A> buildTree = interruptibleTreeBuilder.buildTree(initialState);
				builtTrees.add(buildTree);
				countdownLatch.countDown();
			}));
		}

		try {
			Thread.sleep(maxTreeBuildingTime);
			ConcurrencyUtils.cancelAll(submitedTasks);
			countdownLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		assertNumberBuildTrees(builtTrees);

		LOG.debug("Built trees: " + builtTrees.size());

		return builtTrees;
	}

	private void assertNumberBuildTrees(List<Node<S, A>> builtTrees) {
		if (builtTrees.size() != numberOfParallelTrees) {
			throw new IllegalStateException("Too few trees where build considering the parallelism level. Built: "
					+ builtTrees.size() + " Parallelism level: " + numberOfParallelTrees);
		}
	}
}
