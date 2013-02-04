package wisematches.playground;

import wisematches.core.Personality;
import wisematches.core.PersonalityManager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class AbstractBoardDescription<S extends GameSettings, H extends AbstractPlayerHand> implements BoardDescription<S, H> {
	@Id
	@Column(name = "boardId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	long boardId;

	@Embedded
	S settings;

	@Column(name = "startedDate")
	Date startedDate;

	@Column(name = "finishedDate")
	Date finishedDate;

	@OrderColumn(name = "playerIndex")
	@ElementCollection(fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	@CollectionTable(name = "scribble_player", joinColumns = @JoinColumn(name = "boardId"))
	List<H> hands;

	@Transient
	List<Personality> players;

	@Column(name = "playersCount")
	int playersCount;

	@Column(name = "movesCount")
	int movesCount = 0;

	@Column(name = "currentPlayerIndex")
	int currentPlayerIndex = -1;

	@Column(name = "lastMoveTime")
	Date lastMoveTime;

	@Column(name = "rated")
	boolean rated = true;

	@Column(name = "resolution")
	GameResolution resolution;

	@Embedded
	GameRelationship relationship;

	@Transient
	protected final Lock lock = new ReentrantLock();

	public AbstractBoardDescription() {
	}

	protected void initializePlayers(PersonalityManager playerManager) {
		players = new ArrayList<>(hands.size()); // create new list. It's transient and not stored
		for (H h : hands) {
			final long playerId = h.getPlayerId();
			final Personality player = playerManager.getPerson(playerId);
			if (player == null) {
				throw new IllegalStateException("Player can't be loaded: " + playerId);
			}
			players.add(player);
		}
	}

	@Override
	public S getSettings() {
		return settings;
	}

	@Override
	public long getBoardId() {
		return boardId;
	}

	@Override
	public Date getStartedTime() {
		return startedDate;
	}

	@Override
	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	@Override
	public Date getFinishedTime() {
		return finishedDate;
	}

	@Override
	public GameRelationship getRelationship() {
		return relationship;
	}

	@Override
	public int getMovesCount() {
		lock.lock();
		try {
			return movesCount;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isRated() {
		return rated;
	}

	@Override
	public boolean isActive() {
		lock.lock();
		try {
			return resolution == null && !isGameExpired();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Personality getPlayerTurn() {
		if (currentPlayerIndex < 0) {
			return null;
		}
		return players.get(currentPlayerIndex);
	}

	@Override
	public int getPlayersCount() {
		return hands.size();
	}

	@Override
	public List<Personality> getPlayers() {
		return players;
	}

	@Override
	public H getPlayerHand(Personality player) {
		final int i = players.indexOf(player);
		if (i < 0) {
			return null;
		}
		return hands.get(i);
	}

	@Override
	public GameResolution getResolution() {
		lock.lock();
		try {
			return resolution;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Collection<Personality> getWonPlayers() {
		if (isActive()) {
			return null;
		}

		final Collection<Personality> res = new ArrayList<>();
		for (int i = 0; i < hands.size(); i++) {
			H hand = hands.get(i);
			if (hand.isWinner()) {
				res.add(players.get(i));
			}
		}
		return res;
	}

	protected boolean isGameExpired() {
		return System.currentTimeMillis() - getLastMoveTime().getTime() > settings.getDaysPerMove() * 86400000;
	}
}
