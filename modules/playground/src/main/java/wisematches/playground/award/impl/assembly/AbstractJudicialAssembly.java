package wisematches.playground.award.impl.assembly;

import wisematches.core.Personality;
import wisematches.playground.GameRelationship;
import wisematches.playground.award.AwardWeight;
import wisematches.playground.award.impl.AwardJudicialAssembly;
import wisematches.playground.award.impl.AwardMachinery;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractJudicialAssembly implements AwardJudicialAssembly, AwardMachinery {
	private final Collection<AwardMachinery> machinates = new CopyOnWriteArrayList<>();

	protected AbstractJudicialAssembly() {
	}

	@Override
	public void addAwardMachinery(AwardMachinery machinery) {
		machinates.add(machinery);
	}

	@Override
	public void removeAwardMachinery(AwardMachinery machinery) {
		machinates.remove(machinery);
	}

	@Override
	public void grantAward(Personality person, String code, AwardWeight weight, GameRelationship relationship) {
		for (AwardMachinery machinery : machinates) {
			machinery.grantAward(person, code, weight, relationship);
		}
	}
}
