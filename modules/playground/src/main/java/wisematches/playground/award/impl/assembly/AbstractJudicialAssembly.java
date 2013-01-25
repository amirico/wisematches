package wisematches.playground.award.impl.assembly;

import wisematches.core.Player;
import wisematches.playground.GameRelationship;
import wisematches.playground.award.AwardWeight;
import wisematches.playground.award.impl.AwardExecutiveCommittee;
import wisematches.playground.award.impl.AwardJudicialAssembly;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractJudicialAssembly implements AwardJudicialAssembly, AwardExecutiveCommittee {
	private final Collection<AwardExecutiveCommittee> machinates = new CopyOnWriteArrayList<>();

	protected AbstractJudicialAssembly() {
	}

	@Override
	public void addAwardMachinery(AwardExecutiveCommittee committee) {
		machinates.add(committee);
	}

	@Override
	public void removeAwardMachinery(AwardExecutiveCommittee committee) {
		machinates.remove(committee);
	}

	@Override
	public void grantAward(Player player, String code, AwardWeight weight, GameRelationship relationship) {
		for (AwardExecutiveCommittee machinery : machinates) {
			machinery.grantAward(player, code, weight, relationship);
		}
	}
}
