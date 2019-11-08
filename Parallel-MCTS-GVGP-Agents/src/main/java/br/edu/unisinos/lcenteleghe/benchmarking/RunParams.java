package br.edu.unisinos.lcenteleghe.benchmarking;

import br.edu.unisinos.lcenteleghe.util.PackageNameUtils;
import core.player.AbstractPlayer;

public class RunParams {
	private Class<? extends AbstractPlayer> agentUnderEvaluation;
	private int numThreads;
	private int level;
	private int rep;

	private RunParams(Builder builder) {
		this.agentUnderEvaluation = builder.agentUnderEvaluation;
		this.numThreads = builder.numThreads;
		this.level = builder.level;
		this.rep = builder.rep;
	}

	public Class<? extends AbstractPlayer> getAgentUnderEvaluation() {
		return agentUnderEvaluation;
	}

	public int getNumThreads() {
		return numThreads;
	}

	public int getLevel() {
		return level;
	}

	public int getRep() {
		return rep;
	}

	public void prettyPrint() {
		System.out.println("Run Params: Agent under Evaluation="
				+ PackageNameUtils.extractPackageLastName(agentUnderEvaluation) + " | Level=" + level + " | NumThreads="
				+ numThreads + " | Rep:"+rep);
	}

	@Override
	public String toString() {
		return "RunParams [agentUnderEvaluation=" + PackageNameUtils.extractPackageLastName(agentUnderEvaluation)
				+ ", numThreads=" + numThreads + ", level="
				+ level + "]";
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private Class<? extends AbstractPlayer> agentUnderEvaluation;
		private int numThreads;
		private int level;
		private int rep;

		private Builder() {
		}

		public Builder agentUnderEvaluation(Class<? extends AbstractPlayer> agentUnderEvaluation) {
			this.agentUnderEvaluation = agentUnderEvaluation;
			return this;
		}

		public Builder numThreads(int numThreads) {
			this.numThreads = numThreads;
			return this;
		}

		public Builder level(int level) {
			this.level = level;
			return this;
		}

		public Builder rep(int rep) {
			this.rep = rep;
			return this;
		}

		public RunParams build() {
			return new RunParams(this);
		}
	}
}
