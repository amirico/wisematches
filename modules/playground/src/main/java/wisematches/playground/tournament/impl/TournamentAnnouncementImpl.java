package wisematches.playground.tournament.impl;

import wisematches.personality.Language;
import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentAnnouncement;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentState;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentAnnouncementImpl implements TournamentAnnouncement {
	private int number;
	private Date scheduledDate;

	private final int[][] values = new int[Language.values().length][TournamentSection.values().length];

	public TournamentAnnouncementImpl(Tournament tournament) {
		this.number = tournament.getNumber();
		this.scheduledDate = tournament.getScheduledDate();
	}

	public TournamentAnnouncementImpl(TournamentAnnouncement announcement) {
		this.number = announcement.getNumber();
		this.scheduledDate = announcement.getScheduledDate();

		for (Language language : Language.values()) {
			for (TournamentSection section : TournamentSection.values()) {
				setBoughtTickets(language, section, announcement.getBoughtTickets(language, section));
			}
		}
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Date getScheduledDate() {
		return scheduledDate;
	}

	@Override
	public Date getStartedDate() {
		return null;
	}

	@Override
	public Date getFinishedDate() {
		return null;
	}

	@Override
	public TournamentState getTournamentState() {
		return TournamentState.SCHEDULED;
	}

	@Override
	public int getTotalTickets(Language language) {
		int res = 0;
		for (int value : values[language.ordinal()]) {
			res += value;
		}
		return res;
	}

	@Override
	public int getBoughtTickets(Language language, TournamentSection section) {
		return values[language.ordinal()][section.ordinal()];
	}

	void setBoughtTickets(Language language, TournamentSection section, int count) {
		values[language.ordinal()][section.ordinal()] = count;
	}

	void changeBoughtTickets(Language language, TournamentSection section, int delta) {
		values[language.ordinal()][section.ordinal()] += delta;
	}
}
