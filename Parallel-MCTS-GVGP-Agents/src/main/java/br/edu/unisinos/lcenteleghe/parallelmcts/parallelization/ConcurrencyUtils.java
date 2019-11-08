package br.edu.unisinos.lcenteleghe.parallelmcts.parallelization;

import java.util.List;
import java.util.concurrent.Future;

public class ConcurrencyUtils {


	public static void cancelAll(List<Future<?>> futures) {
		for (Future<?> future : futures) {
			future.cancel(true);
		}
	}
}
