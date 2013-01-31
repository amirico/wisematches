package wisematches.playground.scribble.memory.impl;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.Word;
import wisematches.playground.scribble.memory.MemoryWordManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateMemoryWordManager implements MemoryWordManager {
	private SessionFactory sessionFactory;
	private static final Logger log = Logger.getLogger("wisematches.server.scribble.memory");

	public HibernateMemoryWordManager() {
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void addMemoryWord(ScribbleBoard board, Player player, Word word) {
		if (word == null) {
			throw new NullPointerException("Word is null");
		}
		checkMemoryParameters(board, player);

		if (log.isDebugEnabled()) {
			log.debug("Add new memory word for user " + player + "@" + board.getBoardId() + ": " + word + "@" + word.hashCode());
		}

		final Session session = sessionFactory.getCurrentSession();
		MemoryWord mwdo = (MemoryWord) session.get(MemoryWord.class, new MemoryWord.PK(board.getBoardId(), player.getId(), word));
		if (mwdo == null) {
			mwdo = new MemoryWord(board.getBoardId(), player.getId(), word);
			session.save(mwdo);
		} else {
			mwdo.setWord(word);
			session.update(mwdo);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removeMemoryWord(ScribbleBoard board, Player player, Word word) {
		checkMemoryParameters(board, player);
		if (log.isDebugEnabled()) {
			log.debug("Remove memory word for user " + player + "@" + board.getBoardId() + ": " + word + "@" + word.hashCode());
		}

		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("delete from wisematches.playground.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = :board and memory.wordId.playerId = :pid and memory.wordId.number = :word");
		query.setParameter("board", board.getBoardId());
		query.setParameter("pid", player.getId());
		query.setInteger("word", word.hashCode());
		query.executeUpdate();
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void clearMemoryWords(ScribbleBoard board, Player player) {
		checkMemoryParameters(board, player);

		if (log.isDebugEnabled()) {
			log.debug("Clear memory for user " + player + "@" + board.getBoardId());
		}

		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("delete from wisematches.playground.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = :board and memory.wordId.playerId = :pid");
		query.setParameter("board", board.getBoardId());
		query.setParameter("pid", player.getId());
		query.executeUpdate();
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void clearMemoryWords(ScribbleBoard board) {
		if (board == null) {
			throw new NullPointerException("Board is null");
		}

		if (log.isDebugEnabled()) {
			log.debug("Clear all memory for board " + board.getBoardId());
		}

		final Session session = sessionFactory.getCurrentSession();
		session.createQuery("delete from wisematches.playground.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = :board").setParameter("board", board.getBoardId()).executeUpdate();
	}

	@Override
	public int getMemoryWordsCount(ScribbleBoard board, Player player) {
		checkMemoryParameters(board, player);

		final Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select count(*) from wisematches.playground.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = :board and memory.wordId.playerId = :pid");
		query.setParameter("board", board.getBoardId());
		query.setParameter("pid", player.getId());
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Word> getMemoryWords(ScribbleBoard board, Player player) {
		checkMemoryParameters(board, player);

		final Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select memory.word from wisematches.playground.scribble.memory.impl.MemoryWord memory " +
				"where memory.wordId.boardId = :board and memory.wordId.playerId = :pid");
		query.setParameter("board", board.getBoardId());
		query.setParameter("pid", player.getId());
		final List list = query.list();
		if (list.size() == 0) {
			return Collections.emptyList();
		}
		return list;
	}

	private void checkMemoryParameters(ScribbleBoard board, Personality person) {
		if (board == null) {
			throw new NullPointerException("Board is null");
		}
		if (person == null) {
			throw new NullPointerException("Hand is null");
		}
		if (board.getPlayerHand(person) == null) {
			throw new IllegalArgumentException("Specified hand does not belong to specified board");
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
