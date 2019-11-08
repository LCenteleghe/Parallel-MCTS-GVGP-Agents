package br.edu.unisinos.lcenteleghe.parallelmcts.treebuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.PolicySet;
import br.edu.unisinos.lcenteleghe.parallelmcts.parallelization.ConcurrencyUtils;
import br.edu.unisinos.lcenteleghe.parallelmcts.parallelization.ExecutorServiceFactory;

public class InterruptibleTreeBuilderWithTreeParallelization<S extends ObservableState<S, A>, A extends Enum<A>>
		implements InterruptibleTreeBuilder<S, A> {
	private static final Logger LOG = LoggerFactory.getLogger(InterruptibleTreeBuilderWithTreeParallelization.class);

	private PolicySet<S, A> policySet;

	private int parallelismLevel;

	private InterruptibleTreeBuilderWithTreeParallelization(PolicySet<S, A> policySet, int parallelismLevel) {
		super();
		this.policySet = policySet;
		this.parallelismLevel = parallelismLevel;
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> InterruptibleTreeBuilderWithTreeParallelization<S, A> newInstance(
			PolicySet<S, A> policySet, int parallelismLevel) {
		return new InterruptibleTreeBuilderWithTreeParallelization<>(policySet, parallelismLevel);
	}

	public Node<S, A> buildTree(S initialState) {
		return buildTree(Node.newRootNode(initialState));
	}

	private Node<S, A> buildTree(final Node<S, A> root) {
		BlockingQueue<Node<S, A>> processableNodes = new LinkedBlockingDeque<>(parallelismLevel);

		ExecutorService executorService = ExecutorServiceFactory.getExecutorService();

		List<Future<?>> submitedTasks = new ArrayList<>(parallelismLevel);

		try {
			while (!Thread.currentThread().isInterrupted()) {
				// Select next node to have its default policy reward calculated

				Node<S, A> selected = policySet.getTreePolicy().apply(root);

				// Wait if processable nodes queue is full, avoiding too many tasks from being
				// scheduled
				processableNodes.put(selected);

				// Backup before executing default policy to assign virtual loss
				backUpNumberOfVisits(selected);

				submitedTasks.add(executorService.submit(() -> {
					double reward = policySet.getDefaultPolicy().apply(selected.getState());
					backUpTotalReward(selected, reward);
					processableNodes.poll();
				}));
			}
		} catch (InterruptedException e) {
			LOG.trace("Inturrupted");
			Thread.currentThread().interrupt();
		} finally {
			ConcurrencyUtils.cancelAll(submitedTasks);
		}

		return root;
	}

	private void backUpNumberOfVisits(Node<S, A> selected) {
		Node<S, A> current = selected;
		while (current != null) {
			current.incrementNumberVisits();
			current = current.getParent();
		}
	}

	private void backUpTotalReward(Node<S, A> selected, double reward) {
		Node<S, A> current = selected;
		while (current != null) {
			current.incrementTotalRewardAndUpdateBounds(reward);
			current = current.getParent();
		}
	}
}
