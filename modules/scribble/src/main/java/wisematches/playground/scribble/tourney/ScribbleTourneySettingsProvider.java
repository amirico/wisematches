package wisematches.playground.scribble.tourney;

import wisematches.playground.GameSettingsProvider;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.tourney.regular.TourneyDivision;
import wisematches.playground.tourney.regular.TourneyGroup;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleTourneySettingsProvider implements GameSettingsProvider<ScribbleSettings, TourneyGroup> {
    private int daysPerMove = 3;

    public ScribbleTourneySettingsProvider() {
    }

    @Override
    public ScribbleSettings createGameSettings(TourneyGroup request) {
        final TourneyDivision division = request.getRound().getDivision();
        return new ScribbleSettings("Tourney " + division.getTourney().getNumber(), division.getLanguage(), "ef", daysPerMove);
    }

    public int getDaysPerMove() {
        return daysPerMove;
    }

    public void setDaysPerMove(int daysPerMove) {
        if (daysPerMove < 2) {
            throw new IllegalArgumentException("");
        }
        this.daysPerMove = daysPerMove;
    }
}
