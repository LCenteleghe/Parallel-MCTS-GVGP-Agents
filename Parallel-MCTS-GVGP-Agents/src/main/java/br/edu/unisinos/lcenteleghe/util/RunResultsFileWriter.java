package br.edu.unisinos.lcenteleghe.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.unisinos.lcenteleghe.benchmarking.RunParams;
import br.edu.unisinos.lcenteleghe.benchmarking.RunResults;
import br.edu.unisinos.lcenteleghe.main.ApplicationProperties;

public class RunResultsFileWriter {

	public static void write(RunParams runParams, List<RunResults> runResults) {
		ApplicationProperties applicationProperties = ApplicationProperties.getInstance();
		
		String runId = buildRunId(runParams);

		Path file = Paths.get("results", runId + ".csv");

		List<String> lines = new LinkedList<>();

		lines.add(String.join(";", RunResults.getDefaultHeader()) + ";Threads;MergeMethod;SigmaUct;RunId");

		lines.addAll(
				runResults.stream().map(r -> String.join(";", r.getResultsAsString())
						+ ";" + runParams.getNumThreads()
						+ ";" + applicationProperties.getTreeMergingMethod().name()
						+ ";" + applicationProperties.getSigmaUct()
						+ ";" + runId)
						.collect(Collectors.toList()));

		try {
			Files.write(file, lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String buildRunId(RunParams runParams) {
		return PackageNameUtils.extractPackageLastName(runParams.getAgentUnderEvaluation()) + "_"
				+ "t" + runParams.getNumThreads() + "_"
				+ "l" + runParams.getLevel() + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HH_mm_ss"));
	}

}
