package br.edu.unisinos.lcenteleghe.parallelmcts.uct;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.DefaultPolicy;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.TreePolicy;

public class UctPolicySetBuilder<S extends ObservableState<S, A>, A extends Enum<A>> {
	private TreePolicy<S, A> treePolicy;
	private DefaultPolicy<S, A> defaultPolicy;

	private UctPolicySetBuilder(Builder<S, A> builder) {
		this.treePolicy = builder.treePolicy;
		this.defaultPolicy = builder.defaultPolicy;
	}

	public TreePolicy<S, A> getTreePolicy() {
		return treePolicy;
	}

	public DefaultPolicy<S, A> getDefaultPolicy() {
		return defaultPolicy;
	}

	public static <S extends ObservableState<S, A>, A extends Enum<A>> Builder<S, A> builder() {
		return new Builder<>();
	}
	
	public static final class Builder<S extends ObservableState<S, A>, A extends Enum<A>> {
		private TreePolicy<S, A> treePolicy;
		private DefaultPolicy<S, A> defaultPolicy;

		private Builder() {
		}

		public Builder<S, A> treePolicy(TreePolicy<S, A> treePolicy) {
			this.treePolicy = treePolicy;
			return this;
		}

		public Builder<S, A> defaultPolicy(DefaultPolicy<S, A> defaultPolicy) {
			this.defaultPolicy = defaultPolicy;
			return this;
		}

		public UctPolicySetBuilder<S, A> build() {
			return new UctPolicySetBuilder<>(this);
		}
	}
}
