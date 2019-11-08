package br.edu.unisinos.lcenteleghe.parallelmcts.treebuilders;

import java.util.List;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;
import br.edu.unisinos.lcenteleghe.parallelmcts.treemerger.TreeMerger;

public class SingleTreeBuilderWithRootParallelization<S extends ObservableState<S, A>, A extends Enum<A>>
		implements TreeBuilder<S, A> {
	private ParallelTreesBuilderForRootParallelization<S, A> parallelTreesBuilder;

	private TreeMerger<S, A> treeMerger;

	private SingleTreeBuilderWithRootParallelization(Builder<S, A> builder) {
		this.parallelTreesBuilder = ParallelTreesBuilderForRootParallelization.newInstance(
				builder.interruptibleTreeBuilder,
				builder.parallelismLevel,
				builder.maxTreeBuildingTime);
		this.treeMerger = builder.treeMerger;
	}

	public Node<S, A> buildTree(S initialState) {
		List<Node<S, A>> builtTrees = parallelTreesBuilder.buildTrees(initialState);
		return treeMerger.merge(builtTrees);
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> Builder<S, A> builder() {
		return new Builder<>();
	}

	public static final class Builder<S extends ObservableState<S, A>, A extends Enum<A>> {
		private TreeMerger<S, A> treeMerger;
		private InterruptibleTreeBuilder<S, A> interruptibleTreeBuilder;
		private int parallelismLevel = 1;
		private long maxTreeBuildingTime;

		private Builder() {
		}

		public Builder<S, A> interruptibleTreeBuilder(InterruptibleTreeBuilder<S, A> interruptibleTreeBuilder) {
			this.interruptibleTreeBuilder = interruptibleTreeBuilder;
			return this;
		}

		public Builder<S, A> treeMerger(TreeMerger<S, A> treeMerger) {
			this.treeMerger = treeMerger;
			return this;
		}

		public Builder<S, A> parallelismLevel(int parallelismLevel) {
			this.parallelismLevel = parallelismLevel;
			return this;
		}

		public Builder<S, A> maxTreeBuildingTime(long maxTreeBuildingTime) {
			this.maxTreeBuildingTime = maxTreeBuildingTime;
			return this;
		}

		public SingleTreeBuilderWithRootParallelization<S, A> build() {
			return new SingleTreeBuilderWithRootParallelization<S, A>(this);
		}
	}
}
