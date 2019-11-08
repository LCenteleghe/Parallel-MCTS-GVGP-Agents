package br.edu.unisinos.lcenteleghe.parallelmcts.uct;

import java.util.Objects;
import java.util.Random;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.RandomGeneratorFactory;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.TreePolicy;
import tools.Utils;

public class UctTreePolicy<S extends ObservableState<S, A>, A extends Enum<A>> implements TreePolicy<S, A> {
	private static final double EPSILON = 1e-6;

	private static final double C = Math.sqrt(2);

	private Random rand;

	private UctTreePolicy(Random rand) {
		this.rand = rand;
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> UctTreePolicy<S, A> newInstance() {
		return new UctTreePolicy<>(RandomGeneratorFactory.getRandomGenerator());
	}

	@Override
	public Node<S, A> apply(Node<S, A> node) {
		Node<S, A> cur = node;

		while (!cur.isLeaf()) {
			if (!cur.isFullyExpanded()) {
				return cur.expand();
			} else {
				cur = selectBestChild(cur);
			}
		}

		return cur;
	}

	private Node<S, A> selectBestChild(Node<S, A> node) {
		double bestValue = -Double.MAX_VALUE;
		Node<S, A> selected = null;

		for (Node<S, A> child : node.getChildren()) {
			double uctValue = calculateUctValue(node, child);

			uctValue = Utils.noise(uctValue, EPSILON, rand.nextDouble()); //break ties randomly

			if (uctValue > bestValue) {
				selected = child;
				bestValue = uctValue;
			}
		}

		Objects.requireNonNull(selected, "Unable to find best child node of the node " + node);

		return selected;
	}

	protected double calculateUctValue(Node<S, A> node, Node<S, A> child) {
		return calculateExploitationComponentValue(node, child)
				+
				C * calculateExplorationComponentValue(node, child);
	}

	protected double calculateExplorationComponentValue(Node<S, A> node, Node<S, A> child) {
		return Math.sqrt(Math.log(node.getNumberVisits() + 1.0) / (child.getNumberVisits() + EPSILON));
	}

	protected double calculateExploitationComponentValue(Node<S, A> node, Node<S, A> child) {
		double childValue = child.getTotalValue() / (child.getNumberVisits() + EPSILON);
		return Utils.normalise(childValue, node.getLowerBound(), node.getUpperBound());
	}
}
