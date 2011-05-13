package wisematches.playground.expiration;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameExpirationListener {
	void gameExpiring(long boardId, GameExpirationType expiration);
}
