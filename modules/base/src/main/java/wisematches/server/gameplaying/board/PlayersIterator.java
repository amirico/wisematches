package wisematches.server.gameplaying.board;

import javax.persistence.*;
import java.util.*;

/**
 * Players iterator is a cyclic iterator and always has next element: next player's turn.
 * <p/>
 * This iterator does not have next element if there is no one player of start player was not specified.
 * <p/>
 * This is immutable object and any changes with players requires recreation of this object.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
final class PlayersIterator<P extends GamePlayerHand> implements Iterator<P> {
	@OrderColumn(name = "playerIndex")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "scribble_player", joinColumns = @JoinColumn(name = "boardId"))
	private List<P> playerHands;

	@Column(name = "currentPlayerIndex")
	private int currentPlayerIndex = -1;

	PlayersIterator() {
	}

	/**
	 * Creates new iterator with specified start player.
	 *
	 * @param playerHands  the players hands. The copy of this list will be used.
	 * @param startElement the player who has a turn.
	 */
	public PlayersIterator(List<P> playerHands, P startElement) {
		this.playerHands = new ArrayList<P>(playerHands);

		if (startElement != null) {
			setPlayerTurn(startElement);
		}
	}

	/**
	 * Changes active player in this iterator.
	 *
	 * @param playerHand the player who has turn now.
	 * @throws NoSuchElementException if this iterator does not contains specified player
	 * @throws NullPointerException   if specified player turn is {@code null}
	 */
	public void setPlayerTurn(P playerHand) {
		if (playerHand == null) {
			currentPlayerIndex = -1;
			return;
		}

		final int playerIndex = playerHands.indexOf(playerHand);
		if (playerIndex == -1) {
			throw new NoSuchElementException("If this iterator does not contains specified player");
		}
		currentPlayerIndex = playerIndex;
	}

	/**
	 * Returns player who has a turn.
	 *
	 * @return the player who has a turn.
	 */
	public P getPlayerTurn() {
		if (currentPlayerIndex == -1) {
			return null;
		}
		return playerHands.get(currentPlayerIndex);
	}

	/**
	 * Returns list of all player hands.
	 *
	 * @return the list of all player hands
	 */
	public List<P> getPlayerHands() {
		return Collections.unmodifiableList(playerHands);
	}

	public int getPlayerCode(P player) {
		return playerHands.indexOf(player);
	}

	public int size() {
		return playerHands.size();
	}

	/**
	 * Checks that iterator has next element. This method always returns {@code true} is iterator has
	 * at least one player and player turn is specified.
	 *
	 * @return {@code true} if iterator has players and player turn is specified.
	 * @see #setPlayerTurn(GamePlayerHand)
	 */
	public boolean hasNext() {
		return !playerHands.isEmpty() && currentPlayerIndex != -1;
	}

	/**
	 * Returns next player and set it as a active.
	 *
	 * @return the next player
	 * @see #getPlayerTurn()
	 */
	public P next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No players or current player is not specified");
		}

		currentPlayerIndex++;
		if (currentPlayerIndex == playerHands.size()) {
			currentPlayerIndex = 0;
		}
		return playerHands.get(currentPlayerIndex);
	}

	/**
	 * This method always throws {@code UnsupportedOperationException} because this iterator is immutable.
	 *
	 * @throws UnsupportedOperationException this exception is always thrown.
	 */
	public void remove() {
		throw new UnsupportedOperationException("Remove is unsupported");
	}
}