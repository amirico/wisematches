package wisematches.server.gameplaying.scribble.memory.impl;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.board.BoardStateListener;
import wisematches.server.gameplaying.scribble.Word;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribblePlayerHand;
import wisematches.server.gameplaying.scribble.memory.MemoryWordManager;
import wisematches.server.gameplaying.scribble.room.ScribbleRoomManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateMemoryWordManager extends HibernateDaoSupport implements MemoryWordManager {
	private static final Logger log = Logger.getLogger("wisematches.server.scribble.memory");

	public HibernateMemoryWordManager() {
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void addMemoryWord(ScribbleBoard board, ScribblePlayerHand hand, Word word) {
		if (word == null) {
			throw new NullPointerException("Word is null");
		}
		checkMemoryParameters(board, hand);

		if (log.isDebugEnabled()) {
			log.debug("Add new memory word for user " + hand.getPlayerId() + "@" + board.getBoardId() + ": " + word + "@" + word.hashCode());
		}

		final HibernateTemplate template = getHibernateTemplate();
		MemoryWord mwdo = template.get(MemoryWord.class, new MemoryWord.PK(board.getBoardId(), hand.getPlayerId(), word));
		if (mwdo == null) {
			mwdo = new MemoryWord(board.getBoardId(), hand.getPlayerId(), word);
			template.save(mwdo);
		} else {
			mwdo.setWord(word);
			template.update(mwdo);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removeMemoryWord(ScribbleBoard board, ScribblePlayerHand hand, Word word) {
		checkMemoryParameters(board, hand);
		if (log.isDebugEnabled()) {
			log.debug("Remove memory word for user " + hand.getPlayerId() + "@" + board.getBoardId() + ": " + word + "@" + word.hashCode());
		}

		final HibernateTemplate template = getHibernateTemplate();
		template.bulkUpdate("delete from wisematches.server.gameplaying.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = ? and memory.wordId.playerId = ? and memory.wordId.number = ?",
				board.getBoardId(), hand.getPlayerId(), word.hashCode()
		);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void clearMemoryWords(ScribbleBoard board, ScribblePlayerHand hand) {
		checkMemoryParameters(board, hand);

		if (log.isDebugEnabled()) {
			log.debug("Clear memory for user " + hand.getPlayerId() + "@" + board.getBoardId());
		}

		final HibernateTemplate template = getHibernateTemplate();
		template.bulkUpdate("delete from wisematches.server.gameplaying.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = ? and memory.wordId.playerId = ?",
				board.getBoardId(), hand.getPlayerId()
		);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void clearMemoryWords(ScribbleBoard board) {
		if (board == null) {
			throw new NullPointerException("Board is null");
		}

		if (log.isDebugEnabled()) {
			log.debug("Clear all memory for board " + board.getBoardId());
		}

		final HibernateTemplate template = getHibernateTemplate();
		template.bulkUpdate("delete from wisematches.server.gameplaying.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = ?", board.getBoardId());
	}

	@Override
	public int getMemoryWordsCount(ScribbleBoard board, ScribblePlayerHand hand) {
		checkMemoryParameters(board, hand);

		final HibernateTemplate template = getHibernateTemplate();
		return ((Number) template.find("select count(*) from wisematches.server.gameplaying.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = ? and memory.wordId.playerId = ?",
				board.getBoardId(), hand.getPlayerId()).get(0)).intValue();
	}

	@Override
	public Collection<Word> getMemoryWords(ScribbleBoard board, ScribblePlayerHand hand) {
		checkMemoryParameters(board, hand);

		final HibernateTemplate template = getHibernateTemplate();
		@SuppressWarnings("unchecked")
		final List<Word> list = template.find("select memory.word from wisematches.server.gameplaying.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = ? and memory.wordId.playerId = ?",
				board.getBoardId(), hand.getPlayerId()
		);
		if (list.size() == 0) {
			return Collections.emptyList();
		}
		return list;
	}

	private void checkMemoryParameters(ScribbleBoard board, ScribblePlayerHand hand) {
		if (board == null) {
			throw new NullPointerException("Board is null");
		}
		if (hand == null) {
			throw new NullPointerException("Hand is null");
		}
		if (board.getPlayerHand(hand.getPlayerId()) != hand) {
			throw new IllegalArgumentException("Specified hand does not belong to specified board");
		}
	}
}
