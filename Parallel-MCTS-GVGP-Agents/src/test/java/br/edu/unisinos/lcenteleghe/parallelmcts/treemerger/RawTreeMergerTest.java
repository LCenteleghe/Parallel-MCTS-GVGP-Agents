package br.edu.unisinos.lcenteleghe.parallelmcts.treemerger;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.treemerger.RawTreeMerger;
import tcclab.fixture.ActionsFixture;
import tcclab.fixture.ObservableStateFixture;

public class RawTreeMergerTest {

	@Test
	public void testMerge() throws Exception {
		Node<ObservableStateFixture, ActionsFixture> alpha = Node.newRootNode();
		alpha.addNewChildNode(ActionsFixture.UP, 1, 10);
		alpha.addNewChildNode(ActionsFixture.DOWN, 3, 30);
		alpha.addNewChildNode(ActionsFixture.RIGHT, 4, 40);

		Node<ObservableStateFixture, ActionsFixture> beta = Node.newRootNode();
		beta.addNewChildNode(ActionsFixture.UP, 5, 50);
		beta.addNewChildNode(ActionsFixture.DOWN, 3, 30);
		beta.addNewChildNode(ActionsFixture.LEFT, 3, 30);

		Node<ObservableStateFixture, ActionsFixture> gamma = Node.newRootNode();
		gamma.addNewChildNode(ActionsFixture.UP, 4, 40);
		gamma.addNewChildNode(ActionsFixture.DOWN, 3, 30);
		gamma.addNewChildNode(ActionsFixture.LEFT, 4, 40);

		RawTreeMerger<ObservableStateFixture, ActionsFixture> rawTreeMerger = RawTreeMerger.getInstance();

		Node<ObservableStateFixture, ActionsFixture> newNode = rawTreeMerger
				.merge(Arrays.asList(alpha, beta, gamma));
		
		final int numberOfTrees = 3;

		assertThat(newNode.getChild(ActionsFixture.UP)).isPresent();
		assertThat(newNode.getChild(ActionsFixture.UP).get().getNumberVisits()).isEqualTo(10/numberOfTrees);
		assertThat(newNode.getChild(ActionsFixture.UP).get().getTotalValue()).isEqualTo(100.0/numberOfTrees);

		assertThat(newNode.getChild(ActionsFixture.DOWN)).isPresent();
		assertThat(newNode.getChild(ActionsFixture.DOWN).get().getNumberVisits()).isEqualTo(9/numberOfTrees);
		assertThat(newNode.getChild(ActionsFixture.DOWN).get().getTotalValue()).isEqualTo(90.0/numberOfTrees);

		assertThat(newNode.getChild(ActionsFixture.RIGHT)).isPresent();
		assertThat(newNode.getChild(ActionsFixture.RIGHT).get().getNumberVisits()).isEqualTo(4/numberOfTrees);
		assertThat(newNode.getChild(ActionsFixture.RIGHT).get().getTotalValue()).isEqualTo(40.0/numberOfTrees);

		assertThat(newNode.getChild(ActionsFixture.LEFT)).isPresent();
		assertThat(newNode.getChild(ActionsFixture.LEFT).get().getNumberVisits()).isEqualTo(7/numberOfTrees);
		assertThat(newNode.getChild(ActionsFixture.LEFT).get().getTotalValue()).isEqualTo(70.0/numberOfTrees);
	}

}
