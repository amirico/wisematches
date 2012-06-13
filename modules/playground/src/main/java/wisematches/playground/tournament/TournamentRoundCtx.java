package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentRoundCtx implements TournamentEntityCtx<TournamentRound> {
    private final int tournament;
    private final Language language;
    private final TournamentSection section;
    private final int round;

    public TournamentRoundCtx(int round, TournamentSection section, Language language, int tournament) {
        this.round = round;
        this.section = section;
        this.language = language;
        this.tournament = tournament;
    }

    public int getTournament() {
        return tournament;
    }

    public Language getLanguage() {
        return language;
    }

    public TournamentSection getSection() {
        return section;
    }

    public int getRound() {
        return round;
    }
}
