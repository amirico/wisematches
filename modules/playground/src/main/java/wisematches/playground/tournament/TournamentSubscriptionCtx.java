package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentSubscriptionCtx implements TournamentEntityCtx<TournamentSubscription> {
    private final int tournament;
    private final Language language;
    private final TournamentSection section;

    public TournamentSubscriptionCtx(int tournament, Language language, TournamentSection section) {
        this.tournament = tournament;
        this.language = language;
        this.section = section;
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
}
