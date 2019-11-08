package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import br.edu.unisinos.lcenteleghe.util.ListUtils;

public class Node<S extends ObservableState<S, A>, A extends Enum<A>> {
	private ObservableState<S, A> state;
	private Node<S, A> parent;
	private A sourceAction;
	private Map<Integer, Node<S, A>> childrenMap;
	private int numberVisits;

	private double totalReward;
	private double lowerBound;
	private double upperBound;

	private Queue<A> untriedActions;

	private int depth;

	private Node(Builder<S, A> builder) {
		this.state = builder.state;
		this.parent = builder.parent;
		this.sourceAction = builder.sourceAction;
		this.childrenMap = builder.childrenMap;
		this.numberVisits = builder.numberVisits;
		this.totalReward = builder.totalReward;
		this.lowerBound = builder.lowerBound;
		this.upperBound = builder.upperBound;
		this.untriedActions = builder.untriedActions;
		this.depth = builder.depth;
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> Node<S, A> newRootNode(
			ObservableState<S, A> state) {
		return Node.<S, A>builder()
				.state(state)
				.untriedActions(ListUtils.toRandomizedQueue(state.getAvailableActions(),
						RandomGeneratorFactory.getRandomGenerator()))
				.build();
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> Node<S, A> newRootNode() {
		return Node.<S, A>builder()
				.build();
	}

	private Node<S, A> addNewChildNode(A leadingAction, ObservableState<S, A> childState) {
		Node<S, A> newChild = Node.<S, A>builder()
				.depth(this.getDepth() + 1)
				.parent(this)
				.sourceAction(leadingAction)
				.state(childState)
				.untriedActions(ListUtils.toRandomizedQueue(childState.getAvailableActions(),
						RandomGeneratorFactory.getRandomGenerator()))
				.build();
		
		childrenMap.put(leadingAction.ordinal(), newChild);
		
		return newChild;
	}

	public Node<S, A> addNewChildNode(A leadingAction, int childNumberOfVisits, double childTotalReward) {
		Node<S, A> newChild = Node.<S, A>builder()
				.depth(this.getDepth() + 1)
				.parent(this)
				.sourceAction(leadingAction)
				.numberVisits(childNumberOfVisits)
				.totalReward(childTotalReward)
				.build();
		
		childrenMap.put(leadingAction.ordinal(), newChild);
		
		return newChild;
	}

	public int getDepth() {
		return depth;
	}

	public int getNumberVisits() {
		return numberVisits;
	}

	public synchronized void incrementNumberVisits() {
		numberVisits++;
	}

	public double getTotalValue() {
		return totalReward;
	}

	public Node<S, A> getParent() {
		return parent;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	private void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	private void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	public Collection<Node<S, A>> getChildren() {
		return childrenMap.values();
	}


	public Optional<Node<S, A>> getChild(A sourceAction) {
		return Optional.ofNullable(childrenMap.get(sourceAction.ordinal()));
	}
	
	public boolean isFullyExpanded() {
		return untriedActions.isEmpty();
	}


	public Node<S, A> expand() {
		A nextAction = untriedActions.poll();
		return addNewChildNode(nextAction, getState().copyAsMutable().advanceAndGet(nextAction));
	}

	public ObservableState<S, A> getState() {
		return state;
	}

	private void incrementTotalReward(double valueToAdd) {
		totalReward += valueToAdd;
	}

	private void updateBounds(double reward) {
		if (reward < getLowerBound()) {
			setLowerBound(reward);
		}

		if (reward > getUpperBound()) {
			setUpperBound(reward);
		}
	}

	public synchronized void incrementTotalRewardAndUpdateBounds(double reward) {
		incrementTotalReward(reward);
		updateBounds(reward);
	}

	public boolean isLeaf() {
		return state.isFinal();
	}

	public A getSourceAction() {
		return sourceAction;
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> Builder<S, A> builder() {
		return new Builder<>();
	}

	public static final class Builder<S extends ObservableState<S, A>, A extends Enum<A>> {
		private ObservableState<S, A> state;
		private Node<S, A> parent;
		private A sourceAction;
		private Map<Integer, Node<S, A>> childrenMap = new HashMap<>();
		private double totalReward;
		private double lowerBound;
		private double upperBound;
		private Queue<A> untriedActions;
		private int depth;
		private int numberVisits;

		private Builder() {
		}

		public Builder<S, A> state(ObservableState<S, A> state) {
			this.state = state;
			return this;
		}

		public Builder<S, A> parent(Node<S, A> parent) {
			this.parent = parent;
			return this;
		}

		public Builder<S, A> sourceAction(A sourceAction) {
			this.sourceAction = sourceAction;
			return this;
		}

		public Builder<S, A> childrenMap(Map<Integer, Node<S, A>> childrenMap) {
			this.childrenMap = childrenMap;
			return this;
		}

		public Builder<S, A> numberVisits(int numberVisits) {
			this.numberVisits = numberVisits;
			return this;
		}

		public Builder<S, A> totalReward(double totalReward) {
			this.totalReward = totalReward;
			return this;
		}

		public Builder<S, A> lowerBound(double lowerBound) {
			this.lowerBound = lowerBound;
			return this;
		}

		public Builder<S, A> upperBound(double upperBound) {
			this.upperBound = upperBound;
			return this;
		}

		public Builder<S, A> untriedActions(Queue<A> untriedActions) {
			this.untriedActions = untriedActions;
			return this;
		}

		public Builder<S, A> depth(int depth) {
			this.depth = depth;
			return this;
		}

		public Node<S, A> build() {
			return new Node<>(this);
		}
	}
}
