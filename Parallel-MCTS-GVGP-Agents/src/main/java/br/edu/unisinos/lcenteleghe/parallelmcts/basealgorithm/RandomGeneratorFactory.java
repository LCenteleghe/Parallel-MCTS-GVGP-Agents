package br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomGeneratorFactory {
	public static Random getRandomGenerator() {
		return ThreadLocalRandom.current();
	}
}
