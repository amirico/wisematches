package wisematches.server.services.award.impl.assembly;

import wisematches.core.Player;
import wisematches.playground.GameRelationship;
import wisematches.server.services.award.AwardWeight;
import wisematches.server.services.award.impl.AwardExecutiveCommittee;
import wisematches.server.services.award.impl.AwardJudicialAssembly;

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
