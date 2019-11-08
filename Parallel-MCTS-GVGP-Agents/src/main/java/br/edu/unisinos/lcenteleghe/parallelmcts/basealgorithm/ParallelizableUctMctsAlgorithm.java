package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

import java.util.HashMap;
import java.util.Map;

import br.edu.unisinos.lcenteleghe.main.ApplicationProperties;
import br.edu.unisinos.lcenteleghe.parallelmcts.parallelization.InterruptibleDefaultPolicyWithLeafParallelization;
import br.edu.unisinos.lcenteleghe.parallelmcts.parallelization.ParallelizationTechnique;
import br.edu.unisinos.lcenteleghe.parallelmcts.treebuilders.InterruptibleTreeBuilderWithTreeParallelization;
import br.edu.unisinos.lcenteleghe.parallelmcts.treebuilders.SingleTreeBuilderWithRootParallelization;
import br.edu.unisinos.lcenteleghe.parallelmcts.treebuilders.TreeBuilder;
import br.edu.unisinos.lcenteleghe.parallelmcts.treemerger.TreeMergerFactory;
import br.edu.unisinos.lcenteleghe.parallelmcts.treemerger.TreeMergingMethod;
import br.edu.unisinos.lcenteleghe.parallelmcts.uct.StateObservationEvaluator;
import br.edu.unisinos.lcenteleghe.parallelmcts.uct.UctDefaultPolicy;
import br.edu.unisinos.lcenteleghe.parallelmcts.uct.UctTreePolicy;

public class ParallelizableUctMctsAlgorithm<S extends ObservableState<S, A>, A extends Enum<A>> {
	private BestChildSelector<S, A> bestChildSelector;

	private TreeBuilder<S, A> treeBuilder;

	private ParallelizableUctMctsAlgorithm(Builder<S, A> builder) {
		this.bestChildSelector = builder.bestChildSelector;
		this.treeBuilder = createParallelTreeBuilder(builder, createParallelUctPolicySet(builder));
	}

	public A search(S initialState) {
		Node<S, A> treeRoot = treeBuilder.buildTree(initialState);
		return bestChildSelector.selectBest(treeRoot).getSourceAction();
	}

	private SingleTreeBuilderWithRootParallelization<S, A> createParallelTreeBuilder(Builder<S, A> builder,
			PolicySet<S, A> parallelUctPolicySet) {
		return SingleTreeBuilderWithRootParallelization.<S, A>builder()
				.interruptibleTreeBuilder(InterruptibleTreeBuilderWithTreeParallelization.<S, A>newInstance(
						parallelUctPolicySet, builder.getParallelismLevelFor(ParallelizationTechnique.TREE)))
				.maxTreeBuildingTime(builder.maxTreeBuildingTime)
				.parallelismLevel(builder.getParallelismLevelFor(ParallelizationTechnique.ROOT))
				.treeMerger(TreeMergerFactory.getInstance(builder.treeMergingMethod))
				.build();
	}

	private PolicySet<S, A> createParallelUctPolicySet(Builder<S, A> builder) {
		return PolicySet.<S, A>builder()
				.treePolicy(UctTreePolicy.<S, A>newInstance())
				.defaultPolicy(
						InterruptibleDefaultPolicyWithLeafParallelization.<S, A>builder()
								.baseDefaultPolicy(UctDefaultPolicy.<S, A>builder()
										.maxNumberOfStepsAhead(builder.maxNumberStepsAheadOnSimulation)
										.stateEvaluator(builder.stateObservationEvaluator)
										.build())
								.parallelismLevel(
										builder.getParallelismLevelFor(ParallelizationTechnique.LEAF))
								.build())
				.build();
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> Builder<S, A> builder() {
		return new Builder<>();
	}

	public static final class Builder<S extends ObservableState<S, A>, A extends Enum<A>> {
		private BestChildSelector<S, A> bestChildSelector;
		private StateObservationEvaluator<S> stateObservationEvaluator;
		private int maxNumberStepsAheadOnSimulation = 10;
		private Map<ParallelizationTechnique, Integer> parallelismLevelMap = new HashMap<>();
		private long maxTreeBuildingTime;
		private TreeMergingMethod treeMergingMethod = ApplicationProperties.getInstance().getTreeMergingMethod();

		private Builder() {
		}

		public Builder<S, A> bestChildSelector(BestChildSelector<S, A> bestChildSelector) {
			this.bestChildSelector = bestChildSelector;
			return this;
		}

		public Builder<S, A> stateObservationEvaluator(StateObservationEvaluator<S> stateObservationEvaluator) {
			this.stateObservationEvaluator = stateObservationEvaluator;
			return this;
		}

		public Builder<S, A> maxTreeBuildingTime(long maxTreeBuildingTime) {
			this.maxTreeBuildingTime = maxTreeBuildingTime;
			return this;
		}

//		private Builder<S, A> treeMergingMethod(TreeMergingMethod treeMergingMethod) {
//			this.treeMergingMethod = treeMergingMethod;
//			return this;
//		}

		public Builder<S, A> setParallelismLevelFor(ParallelizationTechnique parallelizationTechnique,
				int parallelismLevel) {
			this.parallelismLevelMap.put(parallelizationTechnique, parallelismLevel);
			return this;
		}

		private int getParallelismLevelFor(ParallelizationTechnique parallelizationTechnique) {
			return parallelismLevelMap.getOrDefault(parallelizationTechnique, 1);
		}

		public ParallelizableUctMctsAlgorithm<S, A> build() {
			return new ParallelizableUctMctsAlgorithm<>(this);
		}
	}
}