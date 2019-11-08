package br.edu.unisinos.lcenteleghe.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class ListUtils {
	
	public static <T> Queue<T> toRandomizedQueue(List<T> list, Random random){
		LinkedList<T> newList = new LinkedList<>(list);
		Collections.shuffle(newList);
		return newList;
	}

	public static <T> T selectRandomValue(List<T> list, Random random) {
		return list.get(random.nextInt(list.size()));
	}
}
