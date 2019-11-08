package br.edu.unisinos.lcenteleghe.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationsPerSecondTracker {
	private long start;
	private AtomicInteger simulationCounter = new AtomicInteger();

	private static SimulationsPerSecondTracker instance = new SimulationsPerSecondTracker();

	public static SimulationsPerSecondTracker getInstance() {
		return instance;
	}

	public void startTracking() {
		simulationCounter = new AtomicInteger();
		start = System.currentTimeMillis();
	}

	public void incrementSimCounter() {
		simulationCounter.incrementAndGet();
	}

	public long stopAndGet() {
		long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start);
		if (totalSeconds == 0) {
			return -1;
		} else {
			return simulationCounter.get() / totalSeconds;
		}
	}
}
