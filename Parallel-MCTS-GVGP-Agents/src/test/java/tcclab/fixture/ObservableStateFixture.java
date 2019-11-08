package tcclab.fixture;

import java.util.List;

import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.MutableState;
import br.edu.unisinos.lcenteleghe.parallelmcts.basealgorithm.ObservableState;
import ontology.Types;
import ontology.Types.ACTIONS;

public class ObservableStateFixture implements ObservableState<ObservableStateFixture, ActionsFixture> {

	@Override
	public boolean isFinal() {
		return false;
	}

	@Override
	public ObservableStateFixture getObservation() {
		return null;
	}

	@Override
	public List<ActionsFixture> getAvailableActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableState<ObservableStateFixture, ActionsFixture> copyAsMutable() {
		// TODO Auto-generated method stub
		return null;
	}




}
