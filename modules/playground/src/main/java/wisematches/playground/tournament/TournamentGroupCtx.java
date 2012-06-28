package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentGroupCtx implements TournamentEntityCtx<TournamentGroup> {
	private final int tournament;
	private final Language language;
	private final TournamentSection section;
	private final int round;
	private final int group;

	private static final long serialVersionUID = -5812555809951477121L;

	public TournamentGroupCtx(TournamentGroup group) {
		this(group.getTournament(), group.getRound(), group.getGroup(), group.getLanguage(), group.getSection());
	}

	public TournamentGroupCtx(int tournament, int round, int group, Language language, TournamentSection section) {
		this.tournament = tournament;
		this.language = language;
		this.section = section;
		this.round = round;
		this.group = group;
	}

	public int getTournament() {
		return tournament;
	}

	public int getRound() {
		return round;
	}

	public int getGroup() {
		return group;
	}

	public Language getLanguage() {
		return language;
	}

	public TournamentSection getSection() {
		return section;
	}
}
