package br.edu.unisinos.lcenteleghe.util;

public class PackageNameUtils {

	public static String extractPackageLastName(Class<?> clazz) {
		String[] strings = clazz.getPackage().getName().split("\\.");
		return strings[strings.length - 1];
	}
}
