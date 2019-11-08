package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

public class PolicySet<S extends ObservableState<S, A>, A extends Enum<A>> {
	private TreePolicy<S, A> treePolicy;
	private DefaultPolicy<S, A> defaultPolicy;

	private PolicySet(Builder<S, A> builder) {
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

		public PolicySet<S, A> build() {
			return new PolicySet<>(this);
		}
	}
}
