package br.edu.unisinos.lcenteleghe.parallelmcts.treemerger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.Node;
import br.edu.unisinos.lcenteleghe.parallelmcts.treemerger.SumTreeMerger;
import tcclab.fixture.ActionsFixture;
import tcclab.fixture.ObservableStateFixture;

public class SumTreeMergerTest {

	@Test
	public void testMerge() throws Exception {
		Node<ObservableStateFixture, ActionsFixture> alpha = Node.<ObservableStateFixture, ActionsFixture>builder()
				.numberVisits(175).build();
		alpha.addNewChildNode(ActionsFixture.UP, 30, 300);
		alpha.addNewChildNode(ActionsFixture.DOWN, 45, 450);
		alpha.addNewChildNode(ActionsFixture.LEFT, 70, 700);
		alpha.addNewChildNode(ActionsFixture.RIGHT, 30, 300);

		Node<ObservableStateFixture, ActionsFixture> beta = Node.<ObservableStateFixture, ActionsFixture>builder()
				.numberVisits(105).build();
		beta.addNewChildNode(ActionsFixture.UP, 20, 200);
		beta.addNewChildNode(ActionsFixture.DOWN, 15, 150);
		beta.addNewChildNode(ActionsFixture.LEFT, 70, 700);

		Node<ObservableStateFixture, ActionsFixture> gamma = Node.<ObservableStateFixture, ActionsFixture>builder()
				.numberVisits(115).build();
		gamma.addNewChildNode(ActionsFixture.UP, 30, 300);
		gamma.addNewChildNode(ActionsFixture.DOWN, 50, 500);
		gamma.addNewChildNode(ActionsFixture.LEFT, 35, 350);


		SumTreeMerger<ObservableStateFixture, ActionsFixture> sumTreeMerger = SumTreeMerger.getInstance();

		Node<ObservableStateFixture, ActionsFixture> newNode = sumTreeMerger
				.merge(Arrays.asList(alpha, beta, gamma));

		assertThat(newNode.getChild(ActionsFixture.UP)).isPresent();
		assertThat(newNode.getChild(ActionsFixture.UP).get().getNumberVisits()).isEqualTo(27);
		assertThat(newNode.getChild(ActionsFixture.UP).get().getTotalValue()).isCloseTo(273.41, within(0.01));

		assertThat(newNode.getChild(ActionsFixture.DOWN)).isPresent();
		assertThat(newNode.getChild(ActionsFixture.DOWN).get().getNumberVisits()).isEqualTo(38);
		assertThat(newNode.getChild(ActionsFixture.DOWN).get().getTotalValue()).isCloseTo(384.81, within(0.01));
		

		assertThat(newNode.getChild(ActionsFixture.LEFT)).isPresent();
		assertThat(newNode.getChild(ActionsFixture.LEFT).get().getNumberVisits()).isEqualTo(59);
		assertThat(newNode.getChild(ActionsFixture.LEFT).get().getTotalValue()).isCloseTo(598.10, within(0.01));

		assertThat(newNode.getChild(ActionsFixture.RIGHT)).isPresent();
		assertThat(newNode.getChild(ActionsFixture.RIGHT).get().getNumberVisits()).isEqualTo(13);
		assertThat(newNode.getChild(ActionsFixture.RIGHT).get().getTotalValue()).isCloseTo(132.91, within(0.01));
	}

}
