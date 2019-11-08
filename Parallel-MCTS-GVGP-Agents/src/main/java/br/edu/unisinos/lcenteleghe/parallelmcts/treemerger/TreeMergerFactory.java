package br.edu.unisinos.lcenteleghe.parallelmcts.treemerger;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;

public class TreeMergerFactory {
	public static <S extends ObservableState<S, A>, A extends Enum<A>> TreeMerger<S, A> getInstance(
			TreeMergingMethod treeMergingMethod) {

		switch (treeMergingMethod) {
		case SUM:
			return SumTreeMerger.getInstance();
		case BEST:
			return BestTreeMerger.getInstance();
		case RAW:
			return RawTreeMerger.getInstance();
		default:
			throw new IllegalStateException("Tree merging method not found: " + treeMergingMethod);
		}
	}
}
