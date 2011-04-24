package wisematches.server.gameplaying.propose.impl;

import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.propose.GameProposal;
import wisematches.server.personality.Personality;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameProposalImpl<S extends GameSettings> implements GameProposal<S>, Serializable {
    private final long id;
    private final S gameSettings;
    private final int playersCount;
    private final Collection<Personality> players = new ArrayList<Personality>();

    protected GameProposalImpl(long id, S gameSettings, int playersCount, Collection<Personality> players) {
        if (gameSettings == null) {
            throw new IllegalArgumentException("");
        }

        this.id = id;
        this.gameSettings = gameSettings;
        this.playersCount = playersCount;
        this.players.addAll(players);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public S getGameSettings() {
        return gameSettings;
    }

    @Override
    public int getPlayersCount() {
        return playersCount;
    }

    @Override
    public Collection<Personality> getPlayers() {
        return Collections.unmodifiableCollection(players);
    }

    @Override
    public boolean containsPlayer(Personality personality) {
        return players.contains(personality);
    }

    void attachPlayer(Personality player) {
    }

    void detachPlayer(Personality player) {
    }
}
