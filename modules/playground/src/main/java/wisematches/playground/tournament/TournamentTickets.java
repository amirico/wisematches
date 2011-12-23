package wisematches.playground.tournament;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentTickets {
	private final long totalTickets;
	private final Map<TournamentSection, Long> sectionsMap = new HashMap<TournamentSection, Long>();

	public TournamentTickets(Map<TournamentSection, Long> sectionsMap) {
		int k = 0;
		this.sectionsMap.putAll(sectionsMap);
		for (Long aLong : sectionsMap.values()) {
			k += aLong;
		}
		totalTickets = k;
	}

	public long getBoughtTicketsCount(TournamentSection section) {
		Long aLong = sectionsMap.get(section);
		if (aLong == null) {
			return 0;
		}
		return aLong;
	}

	public long getTotalTickets() {
		return totalTickets;
	}
}
