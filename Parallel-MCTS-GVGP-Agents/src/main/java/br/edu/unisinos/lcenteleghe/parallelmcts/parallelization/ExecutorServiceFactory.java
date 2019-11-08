package br.edu.unisinos.lcenteleghe.parallelmcts.parallelization;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorServiceFactory {
	static final Logger LOG = LoggerFactory.getLogger(ExecutorServiceFactory.class);

	private static ExecutorService executorService;

	public static ExecutorService getExecutorService() {
		if (executorService == null) {
			throw new IllegalStateException("Factory not started");
		}

		return executorService;
	}

	public static void start() {
		if (executorService == null) {
			createExecutorService();
		}
	}

	private static void createExecutorService() {
		int numberOfCores = Runtime.getRuntime().availableProcessors();
		int numberOfThreads = numberOfCores*4;
		LOG.info("Creating thread pool with {} threads based on the number of processor cores.", numberOfThreads);
		executorService = Executors.newFixedThreadPool(numberOfThreads);
	}
}
