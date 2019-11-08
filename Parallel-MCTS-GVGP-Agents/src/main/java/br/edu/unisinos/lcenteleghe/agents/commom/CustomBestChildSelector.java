package br.edu.unisinos.lcenteleghe.agents.commom;

import java.util.Random;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.BestChildSelector;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.Utils;

public class CustomBestChildSelector implements BestChildSelector<GameState, Types.ACTIONS> {

	private static final double EPSILON = 1e-6;

	private Random rand;

	private CustomBestChildSelector(Random rand) {
		this.rand = rand;
	}

	public static CustomBestChildSelector newInstance(Random rand) {
		return new CustomBestChildSelector(rand);
	}

	@Override
	public Node<GameState, ACTIONS> selectBest(Node<GameState, ACTIONS> node) {
		Node<GameState, ACTIONS> selected = null;
		double bestValue = -Double.MAX_VALUE;
		boolean allEqual = true;
		double first = -1;
		
		for (Node<GameState, ACTIONS> child : node.getChildren()) {
				if (first == -1)
					first =child.getNumberVisits();
				else if (first != child.getNumberVisits()) {
					allEqual = false;
				}

				double childValue = child.getNumberVisits();
				childValue = Utils.noise(childValue, EPSILON, rand.nextDouble()); // break ties randomly
				if (childValue > bestValue) {
					bestValue = childValue;
					selected = child;
				}
		}


		if (selected == null) {
			throw new IllegalStateException("Unexpected Selection");
		} else if (allEqual) {
			return bestAction(node);
		}
		return selected;
	}
	
	
	public Node<GameState, ACTIONS> bestAction(Node<GameState, ACTIONS> node) {
		Node<GameState, ACTIONS> selected = null;
		double bestValue = -Double.MAX_VALUE;

		for (Node<GameState, ACTIONS> child : node.getChildren()) {
				double childValue = child.getTotalValue() / (child.getNumberVisits() + EPSILON);
				childValue = Utils.noise(childValue, EPSILON, rand.nextDouble()); //break ties randomly
				if (childValue > bestValue) {
					bestValue = childValue;
					selected = child;
				}
		}

		if (selected == null) {
			throw new IllegalStateException("Unexpected Selection");
		}

		return selected;
	}

}
