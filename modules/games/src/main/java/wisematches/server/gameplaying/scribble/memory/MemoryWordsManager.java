package wisematches.server.gameplaying.scribble.memory;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribblePlayerHand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MemoryWordsManager extends HibernateDaoSupport {
	public MemoryWordsManager() {
	}

	/**
	 * Adds specified word into players memory in specified board.
	 *
	 * @param board the board.
	 * @param hand  the hand of player in this board.
	 * @param word  the memory word to be added.
	 * @throws NullPointerException	 if any parameter is {@code null}.
	 * @throws IllegalArgumentException if board can't be loaded or player does not belong to specified board.
	 */
	public void addMemoryWord(ScribbleBoard board, ScribblePlayerHand hand, MemoryWord word) {
		if (board == null) {
			throw new NullPointerException("Board is null");
		}
		if (hand == null) {
			throw new NullPointerException("Hand is null");
		}
		if (word == null) {
			throw new NullPointerException("Word is null");
		}
		if (board.getPlayerHand(hand.getPlayerId()) != hand) {
			throw new IllegalArgumentException("Specified hand does not belong to specified board");
		}

		final HibernateTemplate template = getHibernateTemplate();
		MemoryWord mwdo = template.get(MemoryWord.class, new MemoryWord.PK(board.getBoardId(), hand.getPlayerId(), word.getNumber()));
		if (mwdo == null) {
			mwdo = new MemoryWord(board.getBoardId(), hand.getPlayerId(), word.getNumber(), word.getWord());
			template.save(mwdo);
		} else {
			mwdo.setWord(word.getWord());
			template.update(mwdo);
		}
		template.flush();
	}

	public void removeMemoryWord(ScribbleBoard board, ScribblePlayerHand hand, int wordNumber) {
		if (board == null) {
			throw new NullPointerException("Board is null");
		}
		if (hand == null) {
			throw new NullPointerException("Hand is null");
		}
		if (board.getPlayerHand(hand.getPlayerId()) != hand) {
			throw new IllegalArgumentException("Specified hand does not belong to specified board");
		}

		final HibernateTemplate template = getHibernateTemplate();
		final Object o = template.get(MemoryWord.class, new MemoryWord.PK(board.getBoardId(), hand.getPlayerId(), wordNumber));
		if (o != null) {
			template.delete(o);
			template.flush();
		}
	}

	public void clearMemoryWords(ScribbleBoard board, ScribblePlayerHand hand) {
		if (board == null) {
			throw new NullPointerException("Board is null");
		}
		if (hand == null) {
			throw new NullPointerException("Hand is null");
		}
		if (board.getPlayerHand(hand.getPlayerId()) != hand) {
			throw new IllegalArgumentException("Specified hand does not belong to specified board");
		}

		final HibernateTemplate template = getHibernateTemplate();
		template.bulkUpdate("delete from " + MemoryWord.class.getName() + " memory " +
				"where memory.wordId.boardId = ? and memory.wordId.playerId = ?",
				new Object[]{
						board.getBoardId(), hand.getPlayerId()
				}
		);
	}

	public Collection<MemoryWord> getMemoryWords(ScribbleBoard board, ScribblePlayerHand hand) {
		if (board == null) {
			throw new NullPointerException("Board is null");
		}
		if (hand == null) {
			throw new NullPointerException("Hand is null");
		}
		if (board.getPlayerHand(hand.getPlayerId()) != hand) {
			throw new IllegalArgumentException("Specified hand does not belong to specified board");
		}

		final HibernateTemplate template = getHibernateTemplate();

		@SuppressWarnings("unchecked")
		final List<MemoryWord> list = template.find("from " + MemoryWord.class.getName() + " memory " +
				"where memory.wordId.boardId = ? and memory.wordId.playerId = ?",
				new Object[]{
						board.getBoardId(), hand.getPlayerId()
				}
		);

		if (list.size() == 0) {
			return Collections.emptyList();
		}
		final Collection<MemoryWord> res = new ArrayList<MemoryWord>(list.size());
		for (MemoryWord word : list) {
//			res.add(new MemoryWord(word.getNumber(), word.getWord()));
		}
		return res;
	}
}
